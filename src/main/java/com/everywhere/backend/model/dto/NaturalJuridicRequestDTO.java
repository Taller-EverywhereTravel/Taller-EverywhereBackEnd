package com.everywhere.backend.model.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Data
public class NaturalJuridicRequestDTO {
    
    @NotNull(message = "El ID de la persona natural es obligatorio")
    private Integer personNaturalId;
    
    @NotNull(message = "La lista de personas jurídicas es obligatoria")
    private List<Integer> personJuridicIds;
}