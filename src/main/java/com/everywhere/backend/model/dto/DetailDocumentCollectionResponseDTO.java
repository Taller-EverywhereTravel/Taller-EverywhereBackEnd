package com.everywhere.backend.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DetailDocumentCollectionResponseDTO {
    
    private Long id;
    private Integer quantity;
    private String description;
    private BigDecimal price;
    private LocalDateTime dateCreated;
    
    private Long documentCollectionId;
    private String documentCollectionNumber;
    
    private Integer productId;
    private String productDescription;
}