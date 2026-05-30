package com.everywhere.backend.model.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TravelerRequestDTO {
    private LocalDate dateBirth;
    private String nationality;
    private String residence;
    private Integer personNaturalId;
}