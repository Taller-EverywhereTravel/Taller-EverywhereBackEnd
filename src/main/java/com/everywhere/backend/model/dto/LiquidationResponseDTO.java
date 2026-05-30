package com.everywhere.backend.model.dto;
 
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; 
import lombok.Data;
 
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LiquidationResponseDTO {
    private Integer id;
    private String number;
    private LocalDate datePurchase; 
    private String destiny;
    private Integer numberPassenger; 
    private LocalDateTime created;
    private LocalDateTime updated;

    private QuotationResponseDto quotation;
    private ProductResponseDTO product;
    private MethodPaymentResponseDTO methodPayment; 
    private FolderResponseDto folder;
    @JsonIgnore
    private List<ObservationLiquidationResponseDTO> observationLiquidation;

}