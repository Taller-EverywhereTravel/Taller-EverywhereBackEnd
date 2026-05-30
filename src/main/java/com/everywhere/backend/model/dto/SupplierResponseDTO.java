package com.everywhere.backend.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SupplierResponseDTO {

    private Integer id;
    private String name;
    private String nameJuridic;
    private Integer ruc;
    private LocalDateTime created;
    private LocalDateTime updated;
}
