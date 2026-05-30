package com.everywhere.backend.service;

import com.everywhere.backend.model.dto.LiquidationRequestDTO;
import com.everywhere.backend.model.dto.LiquidationResponseDTO;
import com.everywhere.backend.model.dto.LiquidationWithDetailResponseDTO;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface LiquidacionService {
    List<LiquidationResponseDTO> findAll();
    LiquidationResponseDTO findById(Integer id);
    LiquidationWithDetailResponseDTO findByIdWithDetalles(Integer id);
    LiquidationResponseDTO update(Integer id, LiquidationRequestDTO liquidacionRequestDTO);
    void deleteById(Integer id);
    LiquidationResponseDTO create(LiquidationRequestDTO liquidacionRequestDTO, Integer cotizacionId);
    List<LiquidationResponseDTO> findByCarpeta(Integer carpetaId);
    List<LiquidationResponseDTO> findSinCarpeta();
    LiquidationResponseDTO updateCarpeta(Integer id, Integer carpetaId);
    ByteArrayInputStream generateExcel(Integer liquidacionId);
}