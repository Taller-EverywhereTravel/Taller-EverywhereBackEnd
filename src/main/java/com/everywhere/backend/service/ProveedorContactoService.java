package com.everywhere.backend.service;

import com.everywhere.backend.model.dto.SupplierContactRequestDTO;
import com.everywhere.backend.model.dto.SupplierContactResponseDTO;

import java.util.List;

public interface ProveedorContactoService {

    List<SupplierContactResponseDTO> findAll();

    SupplierContactResponseDTO findById(Integer id);

    List<SupplierContactResponseDTO> findByProveedorId(Integer proveedorId);

    List<SupplierContactResponseDTO> findByGrupoContactoId(Integer grupoContactoId);

    SupplierContactResponseDTO save(SupplierContactRequestDTO dto);

    SupplierContactResponseDTO update(Integer id, SupplierContactRequestDTO dto);

    void deleteById(Integer id);
}
