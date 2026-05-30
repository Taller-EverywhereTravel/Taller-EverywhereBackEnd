package com.everywhere.backend.model.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class LiquidationWithDetailResponseDTO {

    private Integer id;
    private String number;
    private LocalDate datePurchase; 
    private String destiny;
    private Integer numberPassenger; 
    private LocalDateTime created;
    private LocalDateTime updated;

    // Relaciones de la liquidación
    private ProductResponseDTO product;
    private MethodPaymentResponseDTO methodPayment;

    // Lista de detalles anidados (SIN la liquidación repetida)
    private List<DetailLiquidationSimpleDTO> detail;

    // Lista de observaciones anidadas (SIN la liquidación repetida)
    private List<ObservationLiquidationSimpleDTO> observation;
}
