package com.everywhere.backend.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class ReceiptResponseDTO {
    private Integer id;
    private String serie;
    private Integer correlative;
    private LocalDate dateIssue;
    private String observation;
    private LocalDate dateExpiration;
    private String fileVenta;
    private String currency;

    private Integer quotationId;
    private String codeQuotation;
    private Integer personId;
    private Integer branchId;
    private Integer methodPaymentId;
    private Integer detailDocumentId;

    private String clientName;
    private String clientDocument;
    private String typeDocumentClient;
    private String branchDescription;
    private String methodPaymentDescription;

    private Integer personJuridicId;
    private String personJuridicRuc;
    private String personJuridicCompanyName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<DetailReceiptResponseDTO> detail;
}
