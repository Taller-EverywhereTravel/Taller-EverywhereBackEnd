package com.everywhere.backend.service;

import com.everywhere.backend.model.dto.DetailDocumentCollectionRequestDTO;
import com.everywhere.backend.model.dto.DetailDocumentCollectionResponseDTO;

import java.util.List;

public interface DetalleDocumentoCobranzaService {
    List<DetailDocumentCollectionResponseDTO> findAll();
    DetailDocumentCollectionResponseDTO findById(Long id);
    List<DetailDocumentCollectionResponseDTO> findByDocumentoCobranzaId(Long documentoId);
    DetailDocumentCollectionResponseDTO save(DetailDocumentCollectionRequestDTO detalleDocumentoCobranzaRequestDTO);
    DetailDocumentCollectionResponseDTO patch(Long id, DetailDocumentCollectionRequestDTO detalleDocumentoCobranzaRequestDTO);
    void deleteById(Long id);
}