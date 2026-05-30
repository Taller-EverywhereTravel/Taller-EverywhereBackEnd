package com.everywhere.backend.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BranchResponseDTO {
    private Integer id;
    private String description;
    private String address;
    private String phone;
    private String mail;
    private Boolean status;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
}
