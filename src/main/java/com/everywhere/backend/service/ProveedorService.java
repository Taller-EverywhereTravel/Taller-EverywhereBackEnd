package com.everywhere.backend.service;

import com.everywhere.backend.model.dto.SupplierRequestDTO;
import com.everywhere.backend.model.dto.SupplierResponseDTO;

import java.util.List;

public interface ProveedorService {

    SupplierResponseDTO create(SupplierRequestDTO proveedorRequestDTO);
    SupplierResponseDTO update(Integer id, SupplierRequestDTO proveedorRequestDTO);
    SupplierResponseDTO getById(Integer id);
    List<SupplierResponseDTO> getAll();
    void delete(Integer id);
}
