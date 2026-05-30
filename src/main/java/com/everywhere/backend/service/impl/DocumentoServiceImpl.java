package com.everywhere.backend.service.impl;

import com.everywhere.backend.exceptions.ResourceNotFoundException;
import com.everywhere.backend.mapper.DocumentoMapper; 
import com.everywhere.backend.model.dto.DocumentRequestDto;
import com.everywhere.backend.model.dto.DocumentResponseDto;
import com.everywhere.backend.model.entity.Document; 
import com.everywhere.backend.repository.DocumentoRepository;
import com.everywhere.backend.service.DocumentoService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentoServiceImpl implements DocumentoService {

    private final DocumentoRepository documentoRepository;
    private final DocumentoMapper documentoMapper;

    @Override
    public List<DocumentResponseDto> findAll() {
        return mapToResponseList(documentoRepository.findAll());
    }

    @Override
    public DocumentResponseDto findById(int id) {
        Document documento = documentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Documento no encontrado con id: " + id));
        return documentoMapper.toResponseDTO(documento);
    }

    @Override
    public DocumentResponseDto create(DocumentRequestDto documentoRequestDto) {
        Document documento = documentoMapper.toEntity(documentoRequestDto); 
        return documentoMapper.toResponseDTO(documentoRepository.save(documento));
    }

    @Override
    public DocumentResponseDto patch(int id, DocumentRequestDto documentoRequestDto) {
        if (!documentoRepository.existsById(id))
            throw new ResourceNotFoundException("Documento no encontrado con id: " + id);

        Document documento = documentoRepository.findById(id).get();
        documentoMapper.updateEntityFromDto(documentoRequestDto, documento); 
        return documentoMapper.toResponseDTO(documentoRepository.save(documento));
    }

    @Override
    public void delete(int id) {
        if (!documentoRepository.existsById(id)) 
            throw new ResourceNotFoundException("Documento no encontrado con id: " + id);
        documentoRepository.deleteById(id);
    }

    private List<DocumentResponseDto> mapToResponseList(List<Document> documentos) {
        return documentos.stream().map(documentoMapper::toResponseDTO).toList();
    }
}