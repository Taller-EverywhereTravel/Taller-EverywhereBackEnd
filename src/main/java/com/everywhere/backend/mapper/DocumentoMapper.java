package com.everywhere.backend.mapper;

import com.everywhere.backend.model.dto.DocumentRequestDto;
import com.everywhere.backend.model.dto.DocumentResponseDto;
import com.everywhere.backend.model.entity.Document;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DocumentoMapper {

    private final ModelMapper modelMapper;

    public Document toEntity(DocumentRequestDto documentoRequestDto) {
        Document documento = modelMapper.map(documentoRequestDto, Document.class);
        return documento;
    }

    public DocumentResponseDto toResponseDTO(Document documento) {
        DocumentResponseDto documentoResponseDto = modelMapper.map(documento, DocumentResponseDto.class);
        return documentoResponseDto;
    }

    public void updateEntityFromDto(DocumentRequestDto documentoRequestDto, Document documento) {
        modelMapper.map(documentoRequestDto, documento);
    }
}