package com.everywhere.backend.service;

import com.everywhere.backend.model.dto.TravelerFrequentRequestDto;
import com.everywhere.backend.model.dto.TravelerFrequentResponseDto;

import java.util.List;

public interface ViajeroFrecuenteService {
    TravelerFrequentResponseDto crear(Integer viajeroId, TravelerFrequentRequestDto viajeroFrecuenteRequestDto);
    List<TravelerFrequentResponseDto> findAll();
    TravelerFrequentResponseDto buscarPorId(Integer id);
    List<TravelerFrequentResponseDto> listarPorViajero(Integer viajeroId);
    void eliminar(Integer id);
    TravelerFrequentResponseDto actualizar(Integer id, TravelerFrequentRequestDto viajeroFrecuenteRequestDto);
    List<TravelerFrequentResponseDto> buscarPorViajeroId(Integer viajeroId);
}