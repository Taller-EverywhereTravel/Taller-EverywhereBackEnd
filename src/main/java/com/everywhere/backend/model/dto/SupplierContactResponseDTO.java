package com.everywhere.backend.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SupplierContactResponseDTO {
    private Integer id;
    private String description;
    private String mail;
    private String number;
    private String codeCountry;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Integer supplierId;
    private String supplierName;
    private Integer groupContactId;
    private String groupContactName;
}
