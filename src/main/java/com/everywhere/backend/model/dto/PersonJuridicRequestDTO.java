package com.everywhere.backend.model.dto;

import lombok.Data; 
import jakarta.validation.Valid;

@Data
public class PersonJuridicRequestDTO {
    private String ruc;
    private String nameCompany;

    @Valid
    private PersonRequestDTO person;
}
