package com.everywhere.backend.model.dto;

import lombok.Data; 
import jakarta.validation.Valid;

@Data
public class PersonNaturalRequestDTO {
    private String document;
    private String name;
    private String surnamePaternal;
    private String surnameMaternal;
    private String sex; 
    private Integer travelerId;
    private Integer categoryPersonId;

    @Valid
    private PersonRequestDTO person;
}