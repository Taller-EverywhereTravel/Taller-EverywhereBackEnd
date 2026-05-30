package com.everywhere.backend.service;

import com.everywhere.backend.model.dto.CategoryPersonaRequestDTO;
import com.everywhere.backend.model.dto.CategoryPersonaResponseDTO;
import com.everywhere.backend.model.dto.PersonNaturalResponseDTO;

import java.util.List;

public interface CategoriaPersonaService {
    List<CategoryPersonaResponseDTO> findAll();
    CategoryPersonaResponseDTO findById(Integer id);
    List<CategoryPersonaResponseDTO> findByNombre(String nombre);
    CategoryPersonaResponseDTO save(CategoryPersonaRequestDTO categoriaPersonaRequestDTO);
    CategoryPersonaResponseDTO patch(Integer id, CategoryPersonaRequestDTO categoriaPersonaRequestDTO);
    void deleteById(Integer id);

    PersonNaturalResponseDTO asignarCategoria(Integer personaNaturalId, Integer categoriaId);
    PersonNaturalResponseDTO desasignarCategoria(Integer personaNaturalId);
    List<PersonNaturalResponseDTO> findPersonasPorCategoria(Integer categoriaId);
    CategoryPersonaResponseDTO getCategoriaDePersona(Integer personaNaturalId);
}