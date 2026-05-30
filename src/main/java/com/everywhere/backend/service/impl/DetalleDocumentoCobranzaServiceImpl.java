package com.everywhere.backend.service.impl;

import com.everywhere.backend.exceptions.ResourceNotFoundException;
import com.everywhere.backend.mapper.DetalleDocumentoCobranzaMapper;
import com.everywhere.backend.model.dto.DetailDocumentCollectionRequestDTO;
import com.everywhere.backend.model.dto.DetailDocumentCollectionResponseDTO;
import com.everywhere.backend.model.entity.DetailDocumentCollection;
import com.everywhere.backend.repository.DetalleDocumentoCobranzaRepository;
import com.everywhere.backend.repository.DocumentoCobranzaRepository;
import com.everywhere.backend.repository.ProductoRepository;
import com.everywhere.backend.service.DetalleDocumentoCobranzaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DetalleDocumentoCobranzaServiceImpl implements DetalleDocumentoCobranzaService {

    private final DetalleDocumentoCobranzaRepository detalleDocumentoCobranzaRepository;
    private final DocumentoCobranzaRepository documentoCobranzaRepository;
    private final ProductoRepository productoRepository;
    private final DetalleDocumentoCobranzaMapper detalleDocumentoCobranzaMapper;

    @Override
    public List<DetailDocumentCollectionResponseDTO> findAll() {
        return mapToResponseList(detalleDocumentoCobranzaRepository.findAllWithRelations());
    }

    @Override
    public DetailDocumentCollectionResponseDTO findById(Long id) {
        DetailDocumentCollection detalle = detalleDocumentoCobranzaRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle no encontrado con ID: " + id));
        return detalleDocumentoCobranzaMapper.toResponseDTO(detalle);
    }

    @Override
    public List<DetailDocumentCollectionResponseDTO> findByDocumentoCobranzaId(Long documentoId) {
        if (!documentoCobranzaRepository.existsById(documentoId))
            throw new ResourceNotFoundException("Documento de cobranza no encontrado con ID: " + documentoId);
        return mapToResponseList(detalleDocumentoCobranzaRepository.findByDocumentCollectionIdWithRelations(documentoId));
    }

    @Override
    @Transactional
    public DetailDocumentCollectionResponseDTO save(DetailDocumentCollectionRequestDTO detalleDocumentoCobranzaRequestDTO) {
        if (!documentoCobranzaRepository.existsById(detalleDocumentoCobranzaRequestDTO.getDocumentCollectionId()))
            throw new ResourceNotFoundException("Documento de cobranza no encontrado con ID: " + detalleDocumentoCobranzaRequestDTO.getDocumentCollectionId());
        
        if (!productoRepository.existsById(detalleDocumentoCobranzaRequestDTO.getProductId()))
            throw new ResourceNotFoundException("Producto no encontrado con ID: " + detalleDocumentoCobranzaRequestDTO.getProductId());

        DetailDocumentCollection detalleDocumentoCobranza = detalleDocumentoCobranzaMapper.toEntity(detalleDocumentoCobranzaRequestDTO);
        detalleDocumentoCobranza.setDocumentCollection(documentoCobranzaRepository.findById(detalleDocumentoCobranzaRequestDTO.getDocumentCollectionId()).get());
        detalleDocumentoCobranza.setProduct(productoRepository.findById(detalleDocumentoCobranzaRequestDTO.getProductId()).get());
 
        return detalleDocumentoCobranzaMapper.toResponseDTO(detalleDocumentoCobranzaRepository.save(detalleDocumentoCobranza));
    }

    @Override
    @Transactional
    public DetailDocumentCollectionResponseDTO patch(Long id, DetailDocumentCollectionRequestDTO detalleDocumentoCobranzaRequestDTO) {
        if (!detalleDocumentoCobranzaRepository.existsById(id))
            throw new ResourceNotFoundException("Detalle no encontrado con ID: " + id);

        DetailDocumentCollection detalleDocumentoCobranza = detalleDocumentoCobranzaRepository.findById(id).get();
        detalleDocumentoCobranzaMapper.updateEntityFromRequest(detalleDocumentoCobranza, detalleDocumentoCobranzaRequestDTO);

        if (detalleDocumentoCobranzaRequestDTO.getDocumentCollectionId() != null) {
            if (!documentoCobranzaRepository.existsById(detalleDocumentoCobranzaRequestDTO.getDocumentCollectionId()))
                throw new ResourceNotFoundException("Documento de cobranza no encontrado con ID: " + detalleDocumentoCobranzaRequestDTO.getDocumentCollectionId());
            detalleDocumentoCobranza.setDocumentCollection(documentoCobranzaRepository.findById(detalleDocumentoCobranzaRequestDTO.getDocumentCollectionId()).get());
        }

        if (detalleDocumentoCobranzaRequestDTO.getProductId() != null) {
            if (!productoRepository.existsById(detalleDocumentoCobranzaRequestDTO.getProductId()))
                throw new ResourceNotFoundException("Producto no encontrado con ID: " + detalleDocumentoCobranzaRequestDTO.getProductId());
            detalleDocumentoCobranza.setProduct(productoRepository.findById(detalleDocumentoCobranzaRequestDTO.getProductId()).get());
        }
 
        return detalleDocumentoCobranzaMapper.toResponseDTO(detalleDocumentoCobranzaRepository.save(detalleDocumentoCobranza));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!detalleDocumentoCobranzaRepository.existsById(id)) 
            throw new ResourceNotFoundException("Detalle no encontrado con ID: " + id);
        detalleDocumentoCobranzaRepository.deleteById(id);
    }

    private List<DetailDocumentCollectionResponseDTO> mapToResponseList(List<DetailDocumentCollection> detalles) {
        return detalles.stream().map(detalleDocumentoCobranzaMapper::toResponseDTO).toList();
    }
}