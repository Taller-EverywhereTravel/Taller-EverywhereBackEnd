package com.everywhere.backend.service;

import com.everywhere.backend.model.dto.DetailDocumentWithPersonDto;
import com.everywhere.backend.model.dto.DetailDocumentRequestDto;
import com.everywhere.backend.model.dto.DetailDocumentResponseDto;
import com.everywhere.backend.model.dto.DetailDocumentSearchDto;

import java.util.List;

public interface DetalleDocumentoService {
    DetailDocumentResponseDto findById(Integer id);
    DetailDocumentResponseDto save(DetailDocumentRequestDto detalleDocumentoRequestDto);
    DetailDocumentResponseDto update(Integer id, DetailDocumentRequestDto detalleDocumentoRequestDto);
    void delete(Integer id);
    List<DetailDocumentResponseDto> findByPersonaId(Integer personaId);
    List<DetailDocumentResponseDto> findAll();
    List<DetailDocumentResponseDto> findByDocumentoId(Integer documentoId);
    List<DetailDocumentResponseDto> findByNumero(String numero);
    List<DetailDocumentResponseDto> findByPersonaNaturalId(Integer personaNaturalId);
    List<DetailDocumentSearchDto> findByPersonaNaturalDocumentoPrefix(String prefijo);
    List<DetailDocumentWithPersonDto> findDocumentosConPersonas();
    List<DetailDocumentWithPersonDto> findDocumentosConPersonasByNumero(String numero);
}