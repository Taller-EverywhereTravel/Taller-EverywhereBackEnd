package com.everywhere.backend.model.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TravelerWithPersonNaturalDTO {
    private Integer id;
    private LocalDate dateBirth;
    private String nationality;
    private String residence;
    private LocalDateTime created;
    private LocalDateTime updated;
    private PersonNaturalWithoutTravelerDTO personNatural;
}