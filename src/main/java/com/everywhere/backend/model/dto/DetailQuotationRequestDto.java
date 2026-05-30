package com.everywhere.backend.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DetailQuotationRequestDto {
    private Integer quantity;
    private Integer unit;
    private String description; 
    private BigDecimal commission;
    private BigDecimal priceHistory;
    private Boolean selected;

    private Integer categoryId;
    private Integer productId;
    private Integer supplierId;
    private Integer operatorId;
}