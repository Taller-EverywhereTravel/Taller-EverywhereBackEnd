package com.everywhere.backend.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OperatorResponseDTO {

    private Integer id;
    private String name;
    private LocalDateTime created;
    private LocalDateTime updated;
}
