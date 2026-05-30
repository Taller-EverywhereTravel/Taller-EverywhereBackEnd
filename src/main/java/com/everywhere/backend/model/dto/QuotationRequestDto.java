package com.everywhere.backend.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class QuotationRequestDto {
    private String nameQuotation;
    private Integer numAdult;
    private Integer numChild;
    private LocalDateTime dateExpiration;
    private String originDestination;
    private LocalDate dateDeparture;
    private LocalDate dateReturn;
    private String currency;
    private String observation;

    private Integer counterId;
    private Integer methodPaymentId;
    private Integer statusQuotationId;
    private Integer branchId;
    private Integer folderId;
}
