package com.everywhere.backend.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FolderResponseDto {
    private int id;
    private String name;
    private String description;
    private LocalDateTime created;
    private LocalDateTime updated;
    private int level;
    private Integer folderFatherId;
}