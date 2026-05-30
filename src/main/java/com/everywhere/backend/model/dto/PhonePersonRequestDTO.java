package com.everywhere.backend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PhonePersonRequestDTO {
    @NotBlank(message = "El número de teléfono no puede estar vacío")
    private String number;
    @NotNull(message = "El código de país no puede ser nulo")
    private String codeCountry;
    @NotBlank(message = "El tipo de teléfono no puede estar vacío")
    private String type;
    private String description;
}
