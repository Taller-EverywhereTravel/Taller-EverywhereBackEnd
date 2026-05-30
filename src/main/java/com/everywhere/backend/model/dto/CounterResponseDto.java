package com.everywhere.backend.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CounterResponseDto {
    private int id;

    private String name;

    private Boolean status;

    private String code;

    private LocalDateTime dateCreation;

    private LocalDateTime dateUpdated;
}
