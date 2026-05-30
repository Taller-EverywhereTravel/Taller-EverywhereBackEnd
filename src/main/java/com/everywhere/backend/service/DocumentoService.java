package com.everywhere.backend.service;

import com.everywhere.backend.model.dto.DocumentRequestDto;
import com.everywhere.backend.model.dto.DocumentResponseDto;

import java.util.List;

public interface DocumentoService {
    List<DocumentResponseDto> findAll();
    DocumentResponseDto findById(int id);
    DocumentResponseDto create(DocumentRequestDto documentoRequestDto);
    DocumentResponseDto patch(int id, DocumentRequestDto documentoRequestDto);
    void delete(int id);
}