package com.everywhere.backend.model.dto;

import lombok.Data;

@Data
public class SupplierCollaboratorRequestDTO {
    private String position;
    private String name;
    private String mail;
    private String phone;
    private String codeCountry;
    private String detail;
    private Integer supplierId;
}
