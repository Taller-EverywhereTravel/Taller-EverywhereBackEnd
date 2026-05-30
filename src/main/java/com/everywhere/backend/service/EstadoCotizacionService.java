package com.everywhere.backend.service;

import com.everywhere.backend.model.dto.StatusQuotationRequestDTO;
import com.everywhere.backend.model.dto.StatusQuotationResponseDTO;

import java.util.List; 

public interface EstadoCotizacionService {

    StatusQuotationResponseDTO create(StatusQuotationRequestDTO dto);

    StatusQuotationResponseDTO update(Integer id, StatusQuotationRequestDTO dto);

    StatusQuotationResponseDTO getById(Integer id);

    List<StatusQuotationResponseDTO> getAll();

    void delete(Integer id);
}

