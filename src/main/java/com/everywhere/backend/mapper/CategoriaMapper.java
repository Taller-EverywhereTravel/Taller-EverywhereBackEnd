package com.everywhere.backend.mapper;

import com.everywhere.backend.model.entity.Category;

import lombok.RequiredArgsConstructor;

import com.everywhere.backend.model.dto.CategoryRequestDto;
import com.everywhere.backend.model.dto.CategoryResponseDto;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoriaMapper {

	private final ModelMapper modelMapper;

	public Category toEntity(CategoryRequestDto categoriaRequestDto) {
		return modelMapper.map(categoriaRequestDto, Category.class);
	}

	public CategoryResponseDto toResponseDto(Category categoria) {
		return modelMapper.map(categoria, CategoryResponseDto.class);
	}

	public void updateEntityFromDTO(CategoryRequestDto categoriaRequestDto, Category categoria) {
		modelMapper.map(categoriaRequestDto, categoria);
	}
}