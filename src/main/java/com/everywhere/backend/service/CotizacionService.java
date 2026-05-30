package com.everywhere.backend.service;

import com.everywhere.backend.model.dto.QuotationRequestDto;
import com.everywhere.backend.model.dto.QuotationResponseDto;
import com.everywhere.backend.model.dto.QuotationWithDetailResponseDTO;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface CotizacionService {
    QuotationResponseDto create(QuotationRequestDto dto, Integer personaId);
    QuotationResponseDto findById(Integer id);
    List<QuotationResponseDto> findAll();
    QuotationResponseDto update(Integer id, QuotationRequestDto dto);
    void delete(Integer id);
    QuotationWithDetailResponseDTO findByIdWithDetalles(Integer id);
    List<QuotationResponseDto> findCotizacionesSinLiquidacion();
    ByteArrayInputStream generateDocx(Integer cotizacionId);
    List<QuotationResponseDto> findByCarpeta(Integer carpetaId);
    List<QuotationResponseDto> findSinCarpeta();
    QuotationResponseDto updateCarpeta(Integer id, Integer carpetaId);
}