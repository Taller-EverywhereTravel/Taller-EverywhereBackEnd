package com.everywhere.backend.service;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.everywhere.backend.model.dto.DocumentCollectionResponseDTO;
import com.everywhere.backend.model.dto.DocumentCollectionUpdateDTO;

public interface DocumentoCobranzaService {
    DocumentCollectionResponseDTO createDocumentoCobranza(Integer cotizacionId, Integer personaJuridicaId, Integer sucursalId);
    ByteArrayInputStream generatePdf(Long documentoId);
    DocumentCollectionResponseDTO findById(Long id);
    DocumentCollectionResponseDTO findBySerieAndCorrelativo(String serie, Integer correlativo);
    List<DocumentCollectionResponseDTO> findAll();
    DocumentCollectionResponseDTO findByCotizacionId(Integer cotizacionId);
    DocumentCollectionResponseDTO patchDocumento(Long id, DocumentCollectionUpdateDTO documentoCobranzaUpdateDTO);
    List<DocumentCollectionResponseDTO> findByCarpeta(Integer carpetaId);
    List<DocumentCollectionResponseDTO> findSinCarpeta();
    DocumentCollectionResponseDTO updateCarpeta(Long id, Integer carpetaId);
}