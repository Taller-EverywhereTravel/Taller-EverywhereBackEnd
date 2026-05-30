package com.everywhere.backend.service;

import com.everywhere.backend.model.dto.SupplierGroupContactRequestDTO;
import com.everywhere.backend.model.dto.SupplierGroupContactResponseDTO;

import java.util.List;

public interface ProveedorGrupoContactoService {

    List<SupplierGroupContactResponseDTO> findAll();

    SupplierGroupContactResponseDTO findById(Integer id);

    List<SupplierGroupContactResponseDTO> findByNombre(String nombre);

    SupplierGroupContactResponseDTO save(SupplierGroupContactRequestDTO dto);

    SupplierGroupContactResponseDTO update(Integer id, SupplierGroupContactRequestDTO dto);

    void deleteById(Integer id);
}
