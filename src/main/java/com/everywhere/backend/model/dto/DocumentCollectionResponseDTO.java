package com.everywhere.backend.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DocumentCollectionResponseDTO {

    private Long id;
    private String serie;
    private Integer correlative;
    private LocalDate dateIssue;
    private String observationes;
    private String fileVenta;
    private BigDecimal costShipping;
    private String currency;

    private Integer quotationId;
    private String codeQuotation;
    private Integer personId;
    private Integer branchId;
    private Integer methodPaymentId;

    private String clientName;
    private String clientDocument;
    private String typeDocumentClient;
    private String branchDescription;
    private String methodPaymentDescription;

    private Integer personJuridicId;
    private String personJuridicRuc;
    private String personJuridicNameCompany;

    private Integer detailDocumentId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<DetailDocumentCollectionResponseDTO> detail;
}