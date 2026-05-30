package com.everywhere.backend.service;

import com.everywhere.backend.model.dto.DetailLiquidationRequestDTO;
import com.everywhere.backend.model.dto.DetailLiquidationResponseDTO;
import com.everywhere.backend.model.dto.DetailLiquidationWithoutLiquidationDTO;

import java.util.List;

public interface DetalleLiquidacionService {

    List<DetailLiquidationResponseDTO> findAll();

    DetailLiquidationResponseDTO findById(Integer id);

    List<DetailLiquidationResponseDTO> findByLiquidacionId(Integer liquidacionId);

    List<DetailLiquidationWithoutLiquidationDTO> findByLiquidacionIdSinLiquidacion(Integer liquidacionId);

    DetailLiquidationResponseDTO save(DetailLiquidationRequestDTO detalleLiquidacionRequestDTO);

    DetailLiquidationResponseDTO update(Integer id, DetailLiquidationRequestDTO detalleLiquidacionRequestDTO);

    void deleteById(Integer id);
}
