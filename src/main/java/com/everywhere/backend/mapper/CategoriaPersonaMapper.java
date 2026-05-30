package com.everywhere.backend.mapper;

import com.everywhere.backend.model.dto.CategoryPersonaRequestDTO;
import com.everywhere.backend.model.dto.CategoryPersonaResponseDTO;
import com.everywhere.backend.model.entity.CategoryPerson;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoriaPersonaMapper {
    private final ModelMapper modelMapper;

    public CategoryPersonaResponseDTO toResponseDTO(CategoryPerson categoriaPersona) {
        return modelMapper.map(categoriaPersona, CategoryPersonaResponseDTO.class);
    }

    public CategoryPerson toEntity(CategoryPersonaRequestDTO categoriaPersonaRequestDTO) {
        return modelMapper.map(categoriaPersonaRequestDTO, CategoryPerson.class);
    }

    public void updateEntityFromDTO(CategoryPersonaRequestDTO categoriaPersonaRequestDTO, CategoryPerson categoriaPersona) {
        modelMapper.map(categoriaPersonaRequestDTO, categoriaPersona);
    }
}