package com.everywhere.backend.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PhonePersonResponseDTO {

    private Integer id;
    private String number;
    private String codeCountry;
    private String type;
    private String description;
    private LocalDateTime created;
    private LocalDateTime updated;
}
