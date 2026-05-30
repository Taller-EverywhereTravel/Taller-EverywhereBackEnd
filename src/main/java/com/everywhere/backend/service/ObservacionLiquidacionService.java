package com.everywhere.backend.service;

import com.everywhere.backend.model.dto.ObservationLiquidationRequestDTO;
import com.everywhere.backend.model.dto.ObservationLiquidationResponseDTO;

import java.util.List;

public interface ObservacionLiquidacionService {

    List<ObservationLiquidationResponseDTO> findAll();
    ObservationLiquidationResponseDTO findById(Long id);
    ObservationLiquidationResponseDTO save(ObservationLiquidationRequestDTO observacionLiquidacionRequestDTO);
    ObservationLiquidationResponseDTO update(Long id, ObservationLiquidationRequestDTO observacionLiquidacionRequestDTO);
    void deleteById(Long id);
    List<ObservationLiquidationResponseDTO> findByLiquidacionId(Integer liquidacionId);
}
