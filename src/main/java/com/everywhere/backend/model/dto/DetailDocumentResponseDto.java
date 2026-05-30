package com.everywhere.backend.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DetailDocumentResponseDto {
    private Integer id;
    private String number;
    private LocalDate dateIssue;  //yyyy-MM-dd
    private LocalDate dateExpiration;
    private String origin;
    private LocalDateTime created;
    private LocalDateTime updated;
    private DocumentResponseDto document;
    private PersonNaturalResponseDTO personNatural;
}