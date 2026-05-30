package com.everywhere.backend.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class QuotationResponseDto {

    private int id;
    private String nameQuotation;
    private String codeQuotation;
    private int numAdult;
    private int numChild;
    private LocalDateTime dateIssue;
    private LocalDateTime dateExpiration;
    private LocalDateTime updated;
    private String originDestination;
    private LocalDate dateDeparture;
    private LocalDate dateReturn;
    private String currency;
    private String observation;

    private CounterResponseDto counter;
    private MethodPaymentResponseDTO methodPayment;
    private StatusQuotationResponseDTO statusQuotation;
    private BranchResponseDTO branch;
    private FolderResponseDto folder;
    private PersonResponseDTO person;
}
