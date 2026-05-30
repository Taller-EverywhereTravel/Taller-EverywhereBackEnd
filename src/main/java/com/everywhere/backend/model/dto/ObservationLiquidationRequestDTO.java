package com.everywhere.backend.model.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ObservationLiquidationRequestDTO {

    private String description;
    private BigDecimal value;
    private String document;
    private String numberDocument;
    private Integer liquidationId;
}
