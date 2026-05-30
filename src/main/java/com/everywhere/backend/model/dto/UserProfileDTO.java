package com.everywhere.backend.model.dto;

import lombok.Data;

@Data
public class UserProfileDTO {
    private Integer id;
    private String name;
    private String mail;
    private String role;
    private BranchResponseDTO branch;
}
