package com.everywhere.backend.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MailPersonResponseDTO {

    private Integer id;
    private String mail;
    private String type;
    private LocalDateTime created;
    private LocalDateTime updated;
}
