package com.everywhere.backend.model.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ReceiptUpdateDTO {
    private LocalDate dateIssue;
    private String fileVenta;
    private String observation;
    private LocalDate dateExpiration;
    private Integer detailDocumentId;
    private Integer branchId;
    private Integer personJuridicId;
    private Integer methodPaymentId;
}
