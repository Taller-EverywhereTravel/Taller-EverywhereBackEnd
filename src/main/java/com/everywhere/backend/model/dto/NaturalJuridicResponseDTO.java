package com.everywhere.backend.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NaturalJuridicResponseDTO {
    
    private Integer id;
    private PersonNaturalResponseDTO personNatural;
    private PersonJuridicResponseDTO personJuridic;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
}