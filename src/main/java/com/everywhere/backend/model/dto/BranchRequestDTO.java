package com.everywhere.backend.model.dto;

import lombok.Data;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

@Data
public class BranchRequestDTO {

    @Size(max = 200, message = "La descripción no puede superar los 200 caracteres")
    private String description;

    @Size(max = 300, message = "La dirección no puede superar los 300 caracteres")
    private String address;

    private String phone;

    @Email(message = "El email debe tener un formato válido")
    @Size(max = 100, message = "El email no puede superar los 100 caracteres")
    private String mail;

    private Boolean status;
}
