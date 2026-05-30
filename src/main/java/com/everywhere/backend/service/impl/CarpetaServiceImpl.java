package com.everywhere.backend.service.impl;

import com.everywhere.backend.mapper.CarpetaMapper;
import com.everywhere.backend.model.dto.FolderRequestDto;
import com.everywhere.backend.model.dto.FolderResponseDto;
import com.everywhere.backend.model.entity.Folder;
import com.everywhere.backend.repository.CarpetaRepository;
import com.everywhere.backend.service.CarpetaService;
import com.everywhere.backend.exceptions.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CarpetaServiceImpl implements CarpetaService {

    private final CarpetaRepository carpetaRepository;
    private final CarpetaMapper carpetaMapper;

    @Override
    @Transactional
    public FolderResponseDto create(FolderRequestDto carpetaRequestDto, Integer carpetaPadreId) {
        Folder carpeta = carpetaMapper.toEntity(carpetaRequestDto);
        
        if (carpetaPadreId != null) { // Primero asignar el nivel correcto
            Folder carpetaPadre = carpetaRepository.findById(carpetaPadreId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Carpeta padre no encontrada con ID: " + carpetaPadreId));
            carpeta.setFolderFather(carpetaPadre);
            carpeta.setLevel(carpetaPadre.getLevel() + 1);
        } else {
            carpeta.setLevel(0); // raíz
        }

        if (carpetaRepository.existsByNameAndLevel(carpeta.getName(), carpeta.getLevel()))
            throw new DataIntegrityViolationException("Ya existe una carpeta con el nombre '" + carpeta.getName() + "' en el nivel " + carpeta.getLevel());

        return carpetaMapper.toResponse(carpetaRepository.save(carpeta));
    }

    @Override
    public FolderResponseDto findById(Integer id) {
        return carpetaRepository.findById(id).map(carpetaMapper::toResponse)
            .orElseThrow(() -> new ResourceNotFoundException("Carpeta no encontrada con ID: " + id));
    }

    @Override
    public List<FolderResponseDto> findAll() {
        return mapToResponseList(carpetaRepository.findAll());
    }

    @Override
    @Transactional
    public FolderResponseDto update(Integer id, FolderRequestDto carpetaRequestDto) {
        Folder carpeta = carpetaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carpeta no encontrada con ID: " + id));

        carpetaMapper.updateEntityFromRequest(carpetaRequestDto, carpeta);
        return carpetaMapper.toResponse(carpetaRepository.save(carpeta));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!carpetaRepository.existsById(id))
            throw new ResourceNotFoundException("Carpeta no encontrada con ID: " + id);
        carpetaRepository.deleteById(id);
    }

    @Override
    public List<FolderResponseDto> findByCarpetaPadreId(Integer carpetaPadreId) {
        if (!carpetaRepository.existsById(carpetaPadreId)) 
            throw new ResourceNotFoundException("Carpeta padre no encontrada con ID: " + carpetaPadreId);
        return mapToResponseList(carpetaRepository.findByFolderFatherId(carpetaPadreId));
    }

    @Override
    public List<FolderResponseDto> findByNivel(Integer nivel) {
        return mapToResponseList(carpetaRepository.findByLevel(nivel));
    }

    @Override
    public List<FolderResponseDto> findByNombre(String nombre) { 
        return mapToResponseList(carpetaRepository.findByNameContainingIgnoreCase(nombre));
    }

    @Override
    public List<FolderResponseDto> findByMes(int mes) {
        int anioActual = LocalDate.now().getYear(); 
        return mapToResponseList(carpetaRepository.findByAnioAndMes(anioActual, mes));
    }

    @Override
    public List<FolderResponseDto> findByFechaCreacionBetween(LocalDate inicio, LocalDate fin) {
        LocalDateTime start = inicio.atStartOfDay();
        LocalDateTime end = fin.plusDays(1).atStartOfDay().minusSeconds(1); 
        return mapToResponseList(carpetaRepository.findByCreatedBetweenOrderByCreatedAsc(start, end));
    }

    @Override
    public List<FolderResponseDto> findRecent(int limit) {
        List<Folder> recientes = carpetaRepository.findAll(PageRequest.of(0, limit, Sort.by("creado").descending())).getContent();
        return mapToResponseList(recientes);
    }

    @Override
    public List<FolderResponseDto> findRaices() { 
        return mapToResponseList(carpetaRepository.findByFolderFatherIsNull());
    }

    @Override
    public List<FolderResponseDto> findCamino(Integer carpetaId) {
        Folder carpeta = carpetaRepository.findById(carpetaId)
                .orElseThrow(() -> new ResourceNotFoundException("Carpeta no encontrada con ID: " + carpetaId));

        List<Folder> camino = new ArrayList<>();

        while (carpeta != null) { // Recorremos hacia arriba hasta la raíz
            camino.add(carpeta);
            carpeta = carpeta.getFolderFather();
        }
        Collections.reverse(camino); // Invertimos para que quede desde la raíz hasta la carpeta actual
        return mapToResponseList(camino);
    }

    @Override
    public List<FolderResponseDto> findHijosByPadreId(Integer carpetaPadreId) {
        if (!carpetaRepository.existsById(carpetaPadreId)) 
            throw new ResourceNotFoundException("Carpeta padre no encontrada con ID: " + carpetaPadreId);
        return mapToResponseList(carpetaRepository.findByFolderFatherId(carpetaPadreId));
    }

    private List<FolderResponseDto> mapToResponseList(List<Folder> carpetas) {
        return carpetas.stream().map(carpetaMapper::toResponse).toList();
    }
}