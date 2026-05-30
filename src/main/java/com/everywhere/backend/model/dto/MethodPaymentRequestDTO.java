package com.everywhere.backend.model.dto;

import lombok.Data;
import jakarta.validation.constraints.Min;

@Data
public class MethodPaymentRequestDTO {
    
    @Min(value = 1, message = "El código debe ser mayor a 0")
    private Integer code;
    
    private String description;
}
