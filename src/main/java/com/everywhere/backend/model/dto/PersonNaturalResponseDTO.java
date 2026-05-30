package com.everywhere.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonNaturalResponseDTO {
    private Integer id;
    private String document;
    private String name;
    private String surnamePaternal;
    private String surnameMaternal;
    private String sex;
    private LocalDateTime created;
    private LocalDateTime updated;
    private PersonResponseDTO person;
    
    @JsonManagedReference("viajero-personaNatural")
    private TravelerResponseDTO traveler;
    
    private CategoryPersonaResponseDTO categoryPerson;
}