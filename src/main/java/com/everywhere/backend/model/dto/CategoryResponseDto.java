package com.everywhere.backend.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CategoryResponseDto {
	private int id;
	private String name;
	private LocalDateTime created;
	private LocalDateTime updated;
}
