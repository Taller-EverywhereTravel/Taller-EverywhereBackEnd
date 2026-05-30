package com.everywhere.backend.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TravelerFrequentResponseDto {
    private Integer id;
    private String airline;
    private String code;
    private TravelerResponseDTO traveler;
    private LocalDateTime created;
    private LocalDateTime updated;
}