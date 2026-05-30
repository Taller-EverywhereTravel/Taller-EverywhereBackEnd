package com.everywhere.backend.service;

import com.everywhere.backend.model.dto.BranchRequestDTO;
import com.everywhere.backend.model.dto.BranchResponseDTO;

import java.util.List;

public interface SucursalService {
    List<BranchResponseDTO> findAll();
    BranchResponseDTO findById(Integer id);
    List<BranchResponseDTO> findByDescripcion(String descripcion);
    BranchResponseDTO findByDescripcionExacta(String descripcion);
    List<BranchResponseDTO> findByEstado(Boolean estado);
    List<BranchResponseDTO> findByEstadoAndDescripcion(Boolean estado, String descripcion);
    List<BranchResponseDTO> findByDireccion(String direccion);
    BranchResponseDTO findByEmail(String email);
    BranchResponseDTO save(BranchRequestDTO sucursalRequestDTO);
    BranchResponseDTO update(Integer id, BranchRequestDTO sucursalRequestDTO);
    void deleteById(Integer id);
    BranchResponseDTO cambiarEstado(Integer id, Boolean estado);
}
