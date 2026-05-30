package com.everywhere.backend.model.dto;

import lombok.Data;

@Data
public class HistorialCotizacionRequestDTO {
    private String observation;
    private Integer userId;
    private Integer quotationId;
    private Integer statusQuotationId;
}