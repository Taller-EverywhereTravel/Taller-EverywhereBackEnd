package com.everywhere.backend.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ProductRequestDTO {
    private String description;
    @NotEmpty(message = "El Producto obligatoriamente tiene que tener un tipo")
    private String type;

}
