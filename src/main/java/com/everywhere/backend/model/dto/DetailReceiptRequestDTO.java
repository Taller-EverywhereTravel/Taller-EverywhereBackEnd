package com.everywhere.backend.model.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class DetailReceiptRequestDTO {
    
    @PositiveOrZero(message = "La cantidad debe ser positiva")
    private Integer amount;

    private String description;
    
    @Positive(message = "El precio debe ser positivo")
    private BigDecimal price;

    private Integer receiptId;
    private Integer productId;
}