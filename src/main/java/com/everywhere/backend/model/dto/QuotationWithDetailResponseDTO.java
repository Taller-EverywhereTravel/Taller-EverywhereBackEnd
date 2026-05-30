package com.everywhere.backend.model.dto;

import com.everywhere.backend.model.entity.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuotationWithDetailResponseDTO {

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

    // Relaciones de la cotización
    private Counter counter;
    private MethodPayment methodPayment;
    private StatusQuotation statusQuotation;
    private Branch branch;
    private Folder folder;
    private Person person;

    // Lista de detalles anidados (SIN la cotización repetida)
    private List<DetailQuotationSimpleDTO> detail;
}