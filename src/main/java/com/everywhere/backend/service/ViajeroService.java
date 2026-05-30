package com.everywhere.backend.service;

import com.everywhere.backend.model.dto.TravelerWithPersonResponseDTO;
import com.everywhere.backend.model.dto.TravelerRequestDTO;
import com.everywhere.backend.model.dto.TravelerResponseDTO;

import java.util.List;

public interface ViajeroService {

    List<TravelerResponseDTO> findAll();
    TravelerResponseDTO findById(Integer id);
    List<TravelerResponseDTO> findByNacionalidad(String nacionalidad);
    List<TravelerResponseDTO> findByResidencia(String residencia);
    TravelerResponseDTO save(TravelerRequestDTO viajeroRequestDTO);
    TravelerResponseDTO patch(Integer id, TravelerRequestDTO viajeroRequestDTO);
    void deleteById(Integer id);
    List<TravelerWithPersonResponseDTO> findAllWithPersonaNatural();
}