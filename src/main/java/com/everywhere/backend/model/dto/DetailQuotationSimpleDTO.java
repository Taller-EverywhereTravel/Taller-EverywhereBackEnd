package com.everywhere.backend.model.dto;

import com.everywhere.backend.model.entity.Category;
import com.everywhere.backend.model.entity.Operator;
import com.everywhere.backend.model.entity.Product;
import com.everywhere.backend.model.entity.Supplier;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DetailQuotationSimpleDTO {

    private int id;
    private Integer quantity;
    private Integer unit;
    private String description;
    private BigDecimal priceHistory;
    private Boolean selected;
    private LocalDateTime created;
    private LocalDateTime updated;
    private BigDecimal comission;

    // Relaciones sin la cotización para evitar referencia circular
    private Category category;
    private Product product;
    private Supplier supplier;
    private Operator operator;
}