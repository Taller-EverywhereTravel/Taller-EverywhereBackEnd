package com.everywhere.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDisplayDto {
    private Integer id;
    private String type;
    private String identifier;
    private String name;
}