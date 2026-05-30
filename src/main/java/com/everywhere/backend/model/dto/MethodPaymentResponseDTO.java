package com.everywhere.backend.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MethodPaymentResponseDTO {
    private Integer id;
    private Integer code;
    private String description;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
}
