package com.everywhere.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentPaxResponseDTO {
    private Integer id;
    private BigDecimal amount;
    private String currency;
    private String detail;
    private LocalDateTime created;
    private LocalDateTime updated;
    private LiquidationResponseDTO liquidation;
    private MethodPaymentResponseDTO methodPayment;
}
