package com.everywhere.backend.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SupplierRequestDTO {
    @NotEmpty(message = "El proveedor obligatoriamente tiene que tener nombre")
    private String name;
    @NotEmpty(message = "El proveedor obligatoriamente tiene que tener nombreJuridico")
    private String nameJuridic;
    @NotEmpty(message = "El proveedor obligatoriamente tiene que tener Ruc")
    private Integer ruc;
}