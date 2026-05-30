package com.everywhere.backend.service;

import com.everywhere.backend.model.dto.FolderRequestDto;
import com.everywhere.backend.model.dto.FolderResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface CarpetaService {

    FolderResponseDto create(FolderRequestDto carpetaRequestDto, Integer carpetaPadreId);
    FolderResponseDto findById(Integer id);
    List<FolderResponseDto> findAll();
    FolderResponseDto update(Integer id, FolderRequestDto carpetaRequestDto);
    void delete(Integer id);
    List<FolderResponseDto> findByCarpetaPadreId(Integer carpetaPadreId);
    List<FolderResponseDto> findByNivel(Integer nivel);
    List<FolderResponseDto> findByNombre(String nombre);
    List<FolderResponseDto> findByMes(int mes);
    List<FolderResponseDto> findByFechaCreacionBetween(LocalDate inicio, LocalDate fin);
    List<FolderResponseDto> findRecent(int limit);
    List<FolderResponseDto> findRaices();
    List<FolderResponseDto> findCamino(Integer carpetaId);
    List<FolderResponseDto> findHijosByPadreId(Integer carpetaPadreId);
}