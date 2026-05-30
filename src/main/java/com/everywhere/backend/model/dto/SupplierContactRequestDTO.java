package com.everywhere.backend.model.dto;

import lombok.Data;

@Data
public class SupplierContactRequestDTO {
    private String description;
    private String mail;
    private String number;
    private String codeCountry;
    private Integer supplierId;
    private Integer groupContactId;
}
