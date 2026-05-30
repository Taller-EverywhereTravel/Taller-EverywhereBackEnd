package com.everywhere.backend.model.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DocumentCollectionUpdateDTO {
    private LocalDate dateIssue;
    private String fileVenta;
    private Double costShipping;
    private String observation;
    private Integer detailDocumentId;
    private Integer branchId;
    private Integer personJuridicId;
    private Integer methodPaymentId;
}