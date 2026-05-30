package com.everywhere.backend.model.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class LiquidationRequestDTO {
    private String number;
    private LocalDate datePurchase; 
    private String destiny;
    private Integer numberPassenger; 

    private Integer quotationId;
    private Integer productId;
    private Integer methodPaymentId;
    private Integer folderId;
}