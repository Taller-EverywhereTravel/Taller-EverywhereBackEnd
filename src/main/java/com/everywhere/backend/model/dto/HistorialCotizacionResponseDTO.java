package com.everywhere.backend.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class HistorialCotizacionResponseDTO {
    private Integer id;
    private UUID uuid;
    private String observation;
    private LocalDateTime dateCreated;

    private Integer userId;
    private String userName;
    private String userMail;

    private Integer quotationId;
    private String codeQuotation;

    private Integer statusQuotationId;
    private String statusQuotationDescription;
}