package com.everywhere.backend.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DetailLiquidationResponseDTO {

    private Integer id;
    private String ticket;
    private String documentCollection;
    private BigDecimal costTicket;
    private BigDecimal chargeService;
    private BigDecimal valueSale;
    private String feeEmision;
    private String documentFee;
    private String comission;
    private String invoicePurchase;
    private String ticketPassenger;
    private BigDecimal amountDiscount;
    private BigDecimal paymentPaxUSD;
    private BigDecimal paymentPaxPEN;
    private LocalDateTime created;
    private LocalDateTime updated;

    private LiquidationResponseDTO liquidation;
    private TravelerResponseDTO traveler;
    private ProductResponseDTO product;
    private SupplierResponseDTO supplier;
    private OperatorResponseDTO operator;
}