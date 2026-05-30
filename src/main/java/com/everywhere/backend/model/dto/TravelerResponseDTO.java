package com.everywhere.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TravelerResponseDTO {
    private Integer id;
    private LocalDate dateBirth;
    private String nationality;
    private String residence;
    private LocalDateTime created;
    private LocalDateTime updated;
    
    @JsonBackReference("viajero-personaNatural")
    private PersonNaturalResponseDTO personNatural;
}