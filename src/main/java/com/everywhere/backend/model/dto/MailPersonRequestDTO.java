package com.everywhere.backend.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MailPersonRequestDTO {
    @NotEmpty(message = "El correo electrónico no puede estar vacío")
    private String mail;
    private String type;
}
