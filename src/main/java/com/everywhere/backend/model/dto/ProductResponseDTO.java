package com.everywhere.backend.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductResponseDTO {

    private int id;
    private String description;
    private String type;
    private LocalDateTime created;
    private LocalDateTime updated;
}
