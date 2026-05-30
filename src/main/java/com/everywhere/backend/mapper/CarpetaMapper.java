package com.everywhere.backend.mapper;

import org.springframework.stereotype.Component;

import com.everywhere.backend.model.dto.FolderRequestDto;
import com.everywhere.backend.model.dto.FolderResponseDto;
import com.everywhere.backend.model.entity.Folder;

import org.modelmapper.ModelMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CarpetaMapper {

    private final ModelMapper modelMapper; 

    public Folder toEntity(FolderRequestDto carpetaRequestDto) {
        return modelMapper.map(carpetaRequestDto, Folder.class);
    }

    public FolderResponseDto toResponse(Folder carpeta) {
        return modelMapper.map(carpeta, FolderResponseDto.class);
    }

    public void updateEntityFromRequest(FolderRequestDto carpetaRequestDto, Folder carpeta) {
        modelMapper.map(carpetaRequestDto, carpeta);
    }
}