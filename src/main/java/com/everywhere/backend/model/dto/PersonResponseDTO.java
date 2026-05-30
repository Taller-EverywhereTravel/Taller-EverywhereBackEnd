package com.everywhere.backend.model.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PersonResponseDTO {
    private Integer id;
    private String address;
    private String observation;
    private LocalDateTime created;
    private LocalDateTime updated;
    private List<PhonePersonResponseDTO> phone;
    private List<MailPersonResponseDTO> mail;
}