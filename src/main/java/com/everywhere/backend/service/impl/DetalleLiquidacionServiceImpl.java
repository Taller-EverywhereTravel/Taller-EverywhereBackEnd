package com.everywhere.backend.service.impl;

import com.everywhere.backend.model.dto.DetailLiquidationRequestDTO;
import com.everywhere.backend.model.dto.DetailLiquidationResponseDTO;
import com.everywhere.backend.model.dto.DetailLiquidationWithoutLiquidationDTO;
import com.everywhere.backend.model.entity.DetailLiquidation;
import com.everywhere.backend.repository.DetalleLiquidacionRepository;
import com.everywhere.backend.repository.LiquidacionRepository;
import com.everywhere.backend.repository.OperadorRepository;
import com.everywhere.backend.repository.ProductoRepository;
import com.everywhere.backend.repository.ProveedorRepository;
import com.everywhere.backend.repository.ViajeroRepository;
import com.everywhere.backend.service.DetalleLiquidacionService;
import com.everywhere.backend.exceptions.ResourceNotFoundException;
import com.everywhere.backend.mapper.DetalleLiquidacionMapper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DetalleLiquidacionServiceImpl implements DetalleLiquidacionService {

    private final DetalleLiquidacionRepository detalleLiquidacionRepository;
    private final DetalleLiquidacionMapper detalleLiquidacionMapper;
    private final LiquidacionRepository liquidacionRepository;
    private final OperadorRepository operadorRepository;
    private final ProveedorRepository proveedorRepository;
    private final ProductoRepository productoRepository;
    private final ViajeroRepository viajeroRepository;


    @Override
    public List<DetailLiquidationResponseDTO> findAll() {
        return mapToResponseList(detalleLiquidacionRepository.findAllWithRelations());
    }

    @Override
    public DetailLiquidationResponseDTO findById(Integer id) {
        DetailLiquidation detalleLiquidacion = detalleLiquidacionRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de liquidación no encontrado con ID: " + id));
        return detalleLiquidacionMapper.toResponseDTO(detalleLiquidacion);
    }

    @Override
    public List<DetailLiquidationResponseDTO> findByLiquidacionId(Integer liquidacionId) {
        return mapToResponseList(detalleLiquidacionRepository.findByLiquidacionIdWithRelations(liquidacionId));
    }

    @Override
    public List<DetailLiquidationWithoutLiquidationDTO> findByLiquidacionIdSinLiquidacion(Integer liquidacionId) {
        return mapToSinLiquidacionList(detalleLiquidacionRepository.findByLiquidacionIdSinLiquidacion(liquidacionId));
    }

    @Override
    public DetailLiquidationResponseDTO save(DetailLiquidationRequestDTO detalleLiquidacionRequestDTO) {
        DetailLiquidation detalleLiquidacion = detalleLiquidacionMapper.toEntity(detalleLiquidacionRequestDTO);

        if(detalleLiquidacionRequestDTO.getLiquidationId() != null) {
            if (!liquidacionRepository.existsById(detalleLiquidacionRequestDTO.getLiquidationId()))
                throw new ResourceNotFoundException("Liquidación no encontrada con ID: " + detalleLiquidacionRequestDTO.getLiquidationId());
            detalleLiquidacion.setLiquidation(liquidacionRepository.findById(detalleLiquidacionRequestDTO.getLiquidationId()).get());
        }

        if (detalleLiquidacionRequestDTO.getOperatorId() != null) {
            if (!operadorRepository.existsById(detalleLiquidacionRequestDTO.getOperatorId()))
                throw new ResourceNotFoundException("Operador no encontrado con ID: " + detalleLiquidacionRequestDTO.getOperatorId());
            detalleLiquidacion.setOperator(operadorRepository.findById(detalleLiquidacionRequestDTO.getOperatorId()).get());
        }

        if (detalleLiquidacionRequestDTO.getSupplierId() != null) {
            if (!proveedorRepository.existsById(detalleLiquidacionRequestDTO.getSupplierId()))
                throw new ResourceNotFoundException("Proveedor no encontrado con ID: " + detalleLiquidacionRequestDTO.getSupplierId());
            detalleLiquidacion.setSupplier(proveedorRepository.findById(detalleLiquidacionRequestDTO.getSupplierId()).get());
        }

        if (detalleLiquidacionRequestDTO.getProductId() != null) {
            if (!productoRepository.existsById(detalleLiquidacionRequestDTO.getProductId()))
                throw new ResourceNotFoundException("Producto no encontrado con ID: " + detalleLiquidacionRequestDTO.getProductId());
            detalleLiquidacion.setProduct(productoRepository.findById(detalleLiquidacionRequestDTO.getProductId()).get());
        }

        if (detalleLiquidacionRequestDTO.getTravelerId() != null) {
            if (!viajeroRepository.existsById(detalleLiquidacionRequestDTO.getTravelerId()))
                throw new ResourceNotFoundException("Viajero no encontrado con ID: " + detalleLiquidacionRequestDTO.getTravelerId());
            detalleLiquidacion.setTraveler(viajeroRepository.findById(detalleLiquidacionRequestDTO.getTravelerId()).get());
        }

        return detalleLiquidacionMapper.toResponseDTO(detalleLiquidacionRepository.save(detalleLiquidacion));
    }

    @Override
    public DetailLiquidationResponseDTO update(Integer id, DetailLiquidationRequestDTO detalleLiquidacionRequestDTO) {
        if (!detalleLiquidacionRepository.existsById(id))
            throw new ResourceNotFoundException("Detalle de liquidación no encontrado con ID: " + id);

        DetailLiquidation detalleLiquidacion = detalleLiquidacionRepository.findById(id).get();
        detalleLiquidacionMapper.updateEntityFromDTO(detalleLiquidacionRequestDTO, detalleLiquidacion);

        if(detalleLiquidacionRequestDTO.getLiquidationId() != null) {
            if (!liquidacionRepository.existsById(detalleLiquidacionRequestDTO.getLiquidationId()))
                throw new ResourceNotFoundException("Liquidación no encontrada con ID: " + detalleLiquidacionRequestDTO.getLiquidationId());
            detalleLiquidacion.setLiquidation(liquidacionRepository.findById(detalleLiquidacionRequestDTO.getLiquidationId()).get());
        }

        if (detalleLiquidacionRequestDTO.getOperatorId() != null) {
            if (!operadorRepository.existsById(detalleLiquidacionRequestDTO.getOperatorId()))
                throw new ResourceNotFoundException("Operador no encontrado con ID: " + detalleLiquidacionRequestDTO.getOperatorId());
            detalleLiquidacion.setOperator(operadorRepository.findById(detalleLiquidacionRequestDTO.getOperatorId()).get());
        }

        if (detalleLiquidacionRequestDTO.getSupplierId() != null) {
            if (!proveedorRepository.existsById(detalleLiquidacionRequestDTO.getSupplierId()))
                throw new ResourceNotFoundException("Proveedor no encontrado con ID: " + detalleLiquidacionRequestDTO.getSupplierId());
            detalleLiquidacion.setSupplier(proveedorRepository.findById(detalleLiquidacionRequestDTO.getSupplierId()).get());
        }

        if (detalleLiquidacionRequestDTO.getProductId() != null) {
            if (!productoRepository.existsById(detalleLiquidacionRequestDTO.getProductId()))
                throw new ResourceNotFoundException("Producto no encontrado con ID: " + detalleLiquidacionRequestDTO.getProductId());
            detalleLiquidacion.setProduct(productoRepository.findById(detalleLiquidacionRequestDTO.getProductId()).get());
        }

        if (detalleLiquidacionRequestDTO.getTravelerId() != null) {
            if (!viajeroRepository.existsById(detalleLiquidacionRequestDTO.getTravelerId()))
                throw new ResourceNotFoundException("Viajero no encontrado con ID: " + detalleLiquidacionRequestDTO.getTravelerId());
            detalleLiquidacion.setTraveler(viajeroRepository.findById(detalleLiquidacionRequestDTO.getTravelerId()).get());
        }

        return detalleLiquidacionMapper.toResponseDTO(detalleLiquidacionRepository.save(detalleLiquidacion));
    }

    @Override
    public void deleteById(Integer id) {
        if (!detalleLiquidacionRepository.existsById(id)) 
            throw new ResourceNotFoundException("Detalle de liquidación no encontrado con ID: " + id);
        detalleLiquidacionRepository.deleteById(id);
    }

    private List<DetailLiquidationResponseDTO> mapToResponseList(List<DetailLiquidation> detalles) {
        return detalles.stream().map(detalleLiquidacionMapper::toResponseDTO).toList();
    }

    private List<DetailLiquidationWithoutLiquidationDTO> mapToSinLiquidacionList(List<DetailLiquidation> detalles) {
        return detalles.stream().map(detalleLiquidacionMapper::toSinLiquidacionDTO).toList();
    }
}