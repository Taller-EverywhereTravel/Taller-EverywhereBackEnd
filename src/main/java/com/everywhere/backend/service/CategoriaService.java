package com.everywhere.backend.service;

import com.everywhere.backend.model.dto.CategoryRequestDto;
import com.everywhere.backend.model.dto.CategoryResponseDto;
import java.util.List;

public interface CategoriaService {
	List<CategoryResponseDto> findAll();
	CategoryResponseDto findById(int id);
	CategoryResponseDto create(CategoryRequestDto categoriaRequestDto);
	CategoryResponseDto patch(int id, CategoryRequestDto categoriaRequestDto);
	void delete(int id);
}