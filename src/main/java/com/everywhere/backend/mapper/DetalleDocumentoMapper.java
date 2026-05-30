package com.everywhere.backend.mapper;

import com.everywhere.backend.model.dto.DetailDocumentRequestDto;
import com.everywhere.backend.model.dto.DetailDocumentResponseDto;
import com.everywhere.backend.model.entity.DetailDocument; 

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DetalleDocumentoMapper {
    
    private final ModelMapper modelMapper;

    @PostConstruct
    public void configureMappings() {
        modelMapper.typeMap(DetailDocumentRequestDto.class, DetailDocument.class).addMappings(mapper -> {
            mapper.skip(DetailDocument::setDocument);
            mapper.skip(DetailDocument::setPersonNatural);
        });
    }

    public DetailDocument toEntity(DetailDocumentRequestDto detalleDocumentoRequestDto) {
        DetailDocument detalleDocumento = modelMapper.map(detalleDocumentoRequestDto, DetailDocument.class);
        return detalleDocumento;
    }

    public DetailDocumentResponseDto toResponse(DetailDocument detalleDocumento) {
        DetailDocumentResponseDto detalleDocumentoResponseDto = modelMapper.map(detalleDocumento, DetailDocumentResponseDto.class);
        return detalleDocumentoResponseDto;
    }

    public void updateEntityFromDto(DetailDocumentRequestDto detalleDocumentoRequestDto, DetailDocument detalleDocumento) {
        modelMapper.map(detalleDocumentoRequestDto, detalleDocumento);
    }
}