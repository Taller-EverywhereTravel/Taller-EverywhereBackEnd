package com.everywhere.backend.service;

import com.everywhere.backend.model.dto.SupplierCollaboratorRequestDTO;
import com.everywhere.backend.model.dto.SupplierCollaboratorResponseDTO;

import java.util.List;

public interface ProveedorColaboradorService {

    List<SupplierCollaboratorResponseDTO> findAll();

    SupplierCollaboratorResponseDTO findById(Integer id);

    List<SupplierCollaboratorResponseDTO> findByProveedorId(Integer proveedorId);

    SupplierCollaboratorResponseDTO save(SupplierCollaboratorRequestDTO dto);

    SupplierCollaboratorResponseDTO update(Integer id, SupplierCollaboratorRequestDTO dto);

    void deleteById(Integer id);
}
