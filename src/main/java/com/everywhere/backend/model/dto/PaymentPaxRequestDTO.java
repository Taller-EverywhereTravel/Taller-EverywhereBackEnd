package com.everywhere.backend.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentPaxRequestDTO {
    
    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.00", inclusive = false, message = "El monto debe ser mayor a 0")
    private BigDecimal amount;

    private String currency;

    private String detail;

    @NotNull(message = "El ID de liquidación es obligatorio")
    private Integer liquidationId;

    @NotNull(message = "El ID de forma de pago es obligatorio")
    private Integer methodPaymentId;
}
