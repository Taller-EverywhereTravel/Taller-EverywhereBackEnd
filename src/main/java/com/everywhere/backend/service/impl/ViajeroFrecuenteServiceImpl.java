package com.everywhere.backend.service.impl;

import com.everywhere.backend.exceptions.ResourceNotFoundException;
import com.everywhere.backend.mapper.ViajeroFrecuenteMapper;
import com.everywhere.backend.model.dto.TravelerFrequentRequestDto;
import com.everywhere.backend.model.dto.TravelerFrequentResponseDto;
import com.everywhere.backend.model.entity.Traveler;
import com.everywhere.backend.model.entity.TravelerFrequent;
import com.everywhere.backend.repository.ViajeroFrecuenteRepository;
import com.everywhere.backend.repository.ViajeroRepository;
import com.everywhere.backend.service.ViajeroFrecuenteService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ViajeroFrecuenteServiceImpl implements ViajeroFrecuenteService {

    private final ViajeroFrecuenteRepository viajeroFrecuenteRepository;
    private final ViajeroRepository viajeroRepository;
    private final ViajeroFrecuenteMapper viajeroFrecuenteMapper;

    @Override
    public List<TravelerFrequentResponseDto> findAll() {
        return mapToResponseList(viajeroFrecuenteRepository.findAll());
    }

    @Override
    public TravelerFrequentResponseDto crear(Integer viajeroId, TravelerFrequentRequestDto viajeroFrecuenteRequestDto) {
        if (viajeroId == null) throw new IllegalArgumentException("El ID del viajero no puede ser nulo");

        if (!viajeroRepository.existsById(viajeroId))
            throw new ResourceNotFoundException("Viajero no encontrado con id: " + viajeroId);

        Traveler viajero = viajeroRepository.findById(viajeroId).get();
        TravelerFrequent viajeroFrecuente = viajeroFrecuenteMapper.toEntity(viajeroFrecuenteRequestDto); 
        viajeroFrecuente.setTraveler(viajero);
        return viajeroFrecuenteMapper.toResponse(viajeroFrecuenteRepository.save(viajeroFrecuente));
    }

    @Override
    public TravelerFrequentResponseDto buscarPorId(Integer id) {
        TravelerFrequent viajeroFrecuente = viajeroFrecuenteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ViajeroFrecuente no encontrado con id: " + id));

        return viajeroFrecuenteMapper.toResponse(viajeroFrecuente);
    }

    @Override
    public List<TravelerFrequentResponseDto> listarPorViajero(Integer viajeroId) {
        return mapToResponseList(viajeroFrecuenteRepository.findByViajero_Id(viajeroId));
    }


    @Override
    public void eliminar(Integer id) {
        if (!viajeroFrecuenteRepository.existsById(id)) throw new ResourceNotFoundException("ViajeroFrecuente no encontrado con id: " + id);
        viajeroFrecuenteRepository.deleteById(id);
    }

    @Override
    public TravelerFrequentResponseDto actualizar(Integer id, TravelerFrequentRequestDto viajeroFrecuenteRequestDto) {
        if (!viajeroFrecuenteRepository.existsById(id))
            throw new ResourceNotFoundException("ViajeroFrecuente no encontrado con id: " + id);

        TravelerFrequent viajeroFrecuente = viajeroFrecuenteRepository.findById(id).get();
        
        if (viajeroFrecuenteRepository.existsByAreolineaAndCodigo(
                viajeroFrecuenteRequestDto.getAirline(),
                viajeroFrecuenteRequestDto.getCode())) {
            throw new IllegalArgumentException(
                    "Ya existe un viajero frecuente con la aerolínea " +
                            viajeroFrecuenteRequestDto.getAirline() +
                            " y el código " + viajeroFrecuenteRequestDto.getCode()
            );
        }

        viajeroFrecuenteMapper.updateEntityFromDto(viajeroFrecuenteRequestDto, viajeroFrecuente);
        return viajeroFrecuenteMapper.toResponse(viajeroFrecuenteRepository.save(viajeroFrecuente));
    }

    @Override
    public List<TravelerFrequentResponseDto> buscarPorViajeroId(Integer viajeroId) {
        return mapToResponseList(viajeroFrecuenteRepository.findByViajero_Id(viajeroId));
    }

    private List<TravelerFrequentResponseDto> mapToResponseList(List<TravelerFrequent> viajerosFrecuentes) {
        return viajerosFrecuentes.stream().map(viajeroFrecuenteMapper::toResponse).toList();
    }
}