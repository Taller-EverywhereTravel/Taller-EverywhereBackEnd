package com.everywhere.backend.model.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DetailDocumentCollectionRequestDTO {
    
    @PositiveOrZero(message = "La cantidad debe ser positiva")
    private Integer quantity;
    
    private String description;
    
    @PositiveOrZero(message = "El precio debe ser positivo")
    private BigDecimal price;
    
    private Long documentCollectionId;
    private Integer productId;
}