package com.everywhere.backend.model.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class StatusQuotationResponseDTO {
    private int id;
    private String description;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
}
