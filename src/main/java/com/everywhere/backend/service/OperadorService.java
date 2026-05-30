package com.everywhere.backend.service;

import com.everywhere.backend.model.dto.OperatorRequestDTO;
import com.everywhere.backend.model.dto.OperatorResponseDTO;

import java.util.List;

public interface OperadorService {

    List<OperatorResponseDTO> findAll();
    OperatorResponseDTO findByNombre(String nombre);
    OperatorResponseDTO findById(int id);
    OperatorResponseDTO save(OperatorRequestDTO dto);
    OperatorResponseDTO update(int id, OperatorRequestDTO dto);
    void deleteById(int id);
}
