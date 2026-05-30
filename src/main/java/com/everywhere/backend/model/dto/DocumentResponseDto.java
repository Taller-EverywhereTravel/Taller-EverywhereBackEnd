package com.everywhere.backend.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocumentResponseDto {
    private int id;
    private String type;
    private String description;
    private Boolean status;
    private LocalDateTime created;
    private LocalDateTime updated;
}
