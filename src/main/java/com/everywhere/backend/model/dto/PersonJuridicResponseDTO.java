package com.everywhere.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonJuridicResponseDTO {
    private Integer id;
    private String ruc;
    private String nameCompany;
    private LocalDateTime created;
    private LocalDateTime updated;
    private PersonResponseDTO person;
}
