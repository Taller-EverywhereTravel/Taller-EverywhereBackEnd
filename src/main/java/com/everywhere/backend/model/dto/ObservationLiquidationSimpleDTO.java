package com.everywhere.backend.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ObservationLiquidationSimpleDTO {

    private Long id;
    private String description;
    private BigDecimal value;
    private String document;
    private String numberDocument;
    private LocalDateTime created;
    private LocalDateTime updated;
    // NO incluye la liquidación para evitar referencia circular
}
