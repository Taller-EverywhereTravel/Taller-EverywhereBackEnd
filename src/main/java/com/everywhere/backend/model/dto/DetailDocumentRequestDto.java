package com.everywhere.backend.model.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DetailDocumentRequestDto {
    @NotBlank(message = "El número de documento es obligatorio")
    private String number;

    private LocalDate dateIssue; // yyyy-MM-dd
    private LocalDate dateExpiration;

    @NotBlank(message = "El país de origen es obligatorio")
    private String origin;

    @NotNull(message = "El tipo de documento es obligatorio")
    private Integer documentId;

    @NotNull(message = "La persona natural es obligatoria")
    private Integer personNaturalId;
}