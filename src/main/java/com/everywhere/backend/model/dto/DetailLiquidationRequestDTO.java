package com.everywhere.backend.model.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DetailLiquidationRequestDTO {
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

    private Integer liquidationId;
    private Integer travelerId;
    private Integer productId;
    private Integer supplierId;
    private Integer operatorId;
}