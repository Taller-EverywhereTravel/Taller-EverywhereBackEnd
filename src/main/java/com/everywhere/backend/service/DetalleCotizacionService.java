package com.everywhere.backend.service;

import com.everywhere.backend.model.dto.DetailQuotationRequestDto;
import com.everywhere.backend.model.dto.DetailQuotationResponseDto;

import java.util.List;

public interface DetalleCotizacionService {

    List<DetailQuotationResponseDto> findAll();

    DetailQuotationResponseDto findById(Integer id);

    List<DetailQuotationResponseDto> findByCotizacionId(Integer cotizacionId);

    DetailQuotationResponseDto create(DetailQuotationRequestDto dto, Integer cotizacionId);

    DetailQuotationResponseDto patch(Integer id, DetailQuotationRequestDto dto);

    void delete(Integer id);
}