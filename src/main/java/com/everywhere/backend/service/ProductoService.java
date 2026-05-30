package com.everywhere.backend.service;

import com.everywhere.backend.model.dto.ProductRequestDTO;
import com.everywhere.backend.model.dto.ProductResponseDTO;

import java.util.List;

public interface ProductoService {

    ProductResponseDTO create(ProductRequestDTO productoRequestDTO);
    ProductResponseDTO update(Integer id, ProductRequestDTO productoRequestDTO);
    ProductResponseDTO getById(Integer id);
    List<ProductResponseDTO> getAll();
    void delete(Integer id);
}
