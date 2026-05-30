package com.everywhere.backend.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SupplierGroupContactResponseDTO {
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime created;
    private LocalDateTime updated;
}
