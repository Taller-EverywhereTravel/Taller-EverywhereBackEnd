package com.everywhere.backend.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SupplierCollaboratorResponseDTO {
    private Integer id;
    private String position;
    private String name;
    private String mail;
    private String phone;
    private String codeCountry;
    private String detail;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Integer supplierId;
    private String supplierName;
}
