package com.everywhere.backend.service.impl;

import com.everywhere.backend.exceptions.ResourceNotFoundException;
import com.everywhere.backend.mapper.DetalleReciboMapper;
import com.everywhere.backend.model.dto.DetailReceiptRequestDTO;
import com.everywhere.backend.model.dto.DetailReceiptResponseDTO;
import com.everywhere.backend.model.entity.DetailReceipt;
import com.everywhere.backend.repository.DetalleReciboRepository;
import com.everywhere.backend.repository.ReciboRepository;
import com.everywhere.backend.repository.ProductoRepository;
import com.everywhere.backend.service.DetalleReciboService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DetalleReciboServiceImpl implements DetalleReciboService {

    private final DetalleReciboRepository detalleReciboRepository;
    private final ReciboRepository reciboRepository;
    private final ProductoRepository productoRepository;
    private final DetalleReciboMapper detalleReciboMapper;

    @Override
    public List<DetailReceiptResponseDTO> findAll() {
        return mapToResponseList(detalleReciboRepository.findAllWithRelations());
    }

    @Override
    public DetailReceiptResponseDTO findById(Integer id) {
        DetailReceipt detalle = detalleReciboRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle no encontrado con ID: " + id));
        return detalleReciboMapper.toResponseDTO(detalle);
    }

    @Override
    public List<DetailReceiptResponseDTO> findByReciboId(Integer reciboId) {
        if (!reciboRepository.existsById(reciboId))
            throw new ResourceNotFoundException("Recibo no encontrado con ID: " + reciboId);
        return mapToResponseList(detalleReciboRepository.findByReciboIdWithRelations(reciboId));
    }

    @Override
    @Transactional
    public DetailReceiptResponseDTO save(DetailReceiptRequestDTO detalleReciboRequestDTO) {
        if (!reciboRepository.existsById(detalleReciboRequestDTO.getReceiptId()))
            throw new ResourceNotFoundException("Recibo no encontrado con ID: " + detalleReciboRequestDTO.getReceiptId());
        
        if (detalleReciboRequestDTO.getProductId() != null && !productoRepository.existsById(detalleReciboRequestDTO.getProductId()))
            throw new ResourceNotFoundException("Producto no encontrado con ID: " + detalleReciboRequestDTO.getProductId());

        DetailReceipt detalleRecibo = detalleReciboMapper.toEntity(detalleReciboRequestDTO);
        detalleRecibo.setReceipt(reciboRepository.findById(detalleReciboRequestDTO.getReceiptId()).get());
        
        if (detalleReciboRequestDTO.getProductId() != null) {
            detalleRecibo.setProduct(productoRepository.findById(detalleReciboRequestDTO.getProductId()).get());
        }
 
        return detalleReciboMapper.toResponseDTO(detalleReciboRepository.save(detalleRecibo));
    }

    @Override
    @Transactional
    public DetailReceiptResponseDTO patch(Integer id, DetailReceiptRequestDTO detalleReciboRequestDTO) {
        if (!detalleReciboRepository.existsById(id))
            throw new ResourceNotFoundException("Detalle no encontrado con ID: " + id);

        DetailReceipt detalleRecibo = detalleReciboRepository.findById(id).get();
        detalleReciboMapper.updateEntityFromRequest(detalleRecibo, detalleReciboRequestDTO);

        if (detalleReciboRequestDTO.getReceiptId() != null) {
            if (!reciboRepository.existsById(detalleReciboRequestDTO.getReceiptId()))
                throw new ResourceNotFoundException("Recibo no encontrado con ID: " + detalleReciboRequestDTO.getReceiptId());
            detalleRecibo.setReceipt(reciboRepository.findById(detalleReciboRequestDTO.getReceiptId()).get());
        }

        if (detalleReciboRequestDTO.getProductId() != null) {
            if (!productoRepository.existsById(detalleReciboRequestDTO.getProductId()))
                throw new ResourceNotFoundException("Producto no encontrado con ID: " + detalleReciboRequestDTO.getProductId());
            detalleRecibo.setProduct(productoRepository.findById(detalleReciboRequestDTO.getProductId()).get());
        }
 
        return detalleReciboMapper.toResponseDTO(detalleReciboRepository.save(detalleRecibo));
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        if (!detalleReciboRepository.existsById(id)) 
            throw new ResourceNotFoundException("Detalle no encontrado con ID: " + id);
        detalleReciboRepository.deleteById(id);
    }

    private List<DetailReceiptResponseDTO> mapToResponseList(List<DetailReceipt> detalles) {
        return detalles.stream().map(detalleReciboMapper::toResponseDTO).toList();
    }
}
