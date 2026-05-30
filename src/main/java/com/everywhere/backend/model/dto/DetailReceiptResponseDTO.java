package com.everywhere.backend.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DetailReceiptResponseDTO {
    private Long id;
    private Integer amount;
    private String description;
    private BigDecimal price;
    private Integer productId;
    private String productDescription;
    private Integer receiptId;
    private String receiptNumber;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
}
