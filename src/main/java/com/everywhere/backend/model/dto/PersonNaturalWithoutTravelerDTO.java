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
public class PersonNaturalWithoutTravelerDTO {
    private Integer id;
    private String document;
    private String name;
    private String surnamePaternal;
    private String surnameMaternal;
    private String sex;
    private LocalDateTime created;
    private LocalDateTime updated;
    private PersonResponseDTO person;
    private CategoryPersonaResponseDTO categoryPerson;
    // SIN campo viajero para evitar referencia circular
}