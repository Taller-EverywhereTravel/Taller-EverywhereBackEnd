package com.everywhere.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TravelerWithPersonResponseDTO {
    private Integer id;
    private LocalDate dateBirth;
    private String nationality;
    private String residence;
    private LocalDateTime created;
    private LocalDateTime updated;
    private PersonNaturalWithoutTravelerResponseDTO personNatural;
}