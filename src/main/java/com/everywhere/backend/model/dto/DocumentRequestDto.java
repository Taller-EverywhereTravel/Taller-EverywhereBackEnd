package com.everywhere.backend.model.dto;

import lombok.Data;

@Data
public class DocumentRequestDto {
    private String type;
    private String description;
    private Boolean status;
}