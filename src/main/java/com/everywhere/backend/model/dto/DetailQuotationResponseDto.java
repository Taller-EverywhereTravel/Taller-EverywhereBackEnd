package com.everywhere.backend.model.dto;

import com.everywhere.backend.model.entity.Category;
import com.everywhere.backend.model.entity.Quotation;
import com.everywhere.backend.model.entity.Operator;
import com.everywhere.backend.model.entity.Product;
import com.everywhere.backend.model.entity.Supplier;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DetailQuotationResponseDto {
    private int id;
    private Integer quantity;
    private Integer unit;
    private String description;
    private BigDecimal priceHistory;
    private Boolean selected;
    private LocalDateTime created;
    private LocalDateTime updated;
    private BigDecimal comission;
    private Category category;
    private Quotation quotation;
    private Product product;
    private Supplier supplier;
    private Operator operator;
}