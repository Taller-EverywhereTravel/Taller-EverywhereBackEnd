package com.everywhere.backend.service.impl;

import com.everywhere.backend.mapper.PersonaNaturalMapper;
import com.everywhere.backend.model.dto.TravelerWithPersonResponseDTO;
import com.everywhere.backend.model.dto.TravelerRequestDTO;
import com.everywhere.backend.model.dto.TravelerResponseDTO;
import com.everywhere.backend.model.entity.Traveler;
import com.everywhere.backend.model.entity.PersonNatural;
import com.everywhere.backend.repository.ViajeroRepository;
import com.everywhere.backend.repository.PersonaNaturalRepository;
import com.everywhere.backend.service.ViajeroService;
import com.everywhere.backend.exceptions.ResourceNotFoundException;
import com.everywhere.backend.mapper.ViajeroMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ViajeroServiceImpl implements ViajeroService {

    private final ViajeroRepository viajeroRepository;
    private final PersonaNaturalRepository personaNaturalRepository;
    private final PersonaNaturalMapper personaNaturalMapper;
    private final ViajeroMapper viajeroMapper;

    @Override
    public List<TravelerResponseDTO> findAll() {
        return mapToResponseList(viajeroRepository.findAll());
    }

    @Override
    public TravelerResponseDTO findById(Integer id) {
        Traveler viajero = viajeroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Viajero no encontrado con ID: " + id));
        return viajeroMapper.toResponseDTO(viajero);
    }

    @Override
    public List<TravelerResponseDTO> findByNacionalidad(String nacionalidad) {
        return mapToResponseList(viajeroRepository.findByNacionalidadIgnoreAccents(nacionalidad));
    }

    @Override
    public List<TravelerResponseDTO> findByResidencia(String residencia) {
        return mapToResponseList(viajeroRepository.findByResidenciaIgnoreAccents(residencia));
    }

    @Override
    public TravelerResponseDTO save(TravelerRequestDTO viajeroRequestDTO) {
        Traveler viajero = viajeroMapper.toEntity(viajeroRequestDTO);
        // Si viene personaNaturalId, buscar la PersonaNatural y enlazarla
        if (viajeroRequestDTO.getPersonNaturalId() != null) {
            PersonNatural personaNatural = personaNaturalRepository.findById(viajeroRequestDTO.getPersonNaturalId())
                    .orElseThrow(() -> new DataIntegrityViolationException("PersonaNatural no encontrada con ID: " + viajeroRequestDTO.getPersonNaturalId()));

            // Guardar primero el viajero (aún sin asignar en la persona)
            Traveler savedViajero = viajeroRepository.save(viajero);

            // Ahora asignar la referencia en la entidad propietaria (PersonaNatural) y guardar
            personaNatural.setTraveler(savedViajero);
            personaNaturalRepository.save(personaNatural);

            // Asegurar que el DTO resultante refleje ambas relaciones
            savedViajero.setPersonNatural(personaNatural);
            return viajeroMapper.toResponseDTO(savedViajero);
        }

        Traveler savedViajero = viajeroRepository.save(viajero);
        return viajeroMapper.toResponseDTO(savedViajero);
    }

    @Override
    public TravelerResponseDTO patch(Integer id, TravelerRequestDTO viajeroRequestDTO) {
        if (!viajeroRepository.existsById(id))
            throw new ResourceNotFoundException("Viajero no encontrado con ID: " + id);

        Traveler existingViajero = viajeroRepository.findById(id).get();
        viajeroMapper.updateEntityFromDTO(viajeroRequestDTO, existingViajero);
        existingViajero = viajeroRepository.save(existingViajero);
        return viajeroMapper.toResponseDTO(existingViajero);
    }

    @Override
    public void deleteById(Integer id) {
        if (!viajeroRepository.existsById(id))
            throw new ResourceNotFoundException("Viajero no encontrado con ID: " + id);
        viajeroRepository.deleteById(id);
    }

    private List<TravelerResponseDTO> mapToResponseList(List<Traveler> viajeros) {
        return viajeros.stream().map(viajeroMapper::toResponseDTO).toList();
    }

    @Override
    public List<TravelerWithPersonResponseDTO> findAllWithPersonaNatural() {
        return viajeroRepository.findAll().stream()
                .filter(viajero -> viajero.getPersonNatural() != null)
                .map(viajero -> TravelerWithPersonResponseDTO.builder()
                        .id(viajero.getId())
                        .dateBirth(viajero.getDateBirth())
                        .nationality(viajero.getNationality())
                        .residence(viajero.getResidence())
                        .created(viajero.getCreated())
                        .updated(viajero.getUpdated())
                        .personNatural(personaNaturalMapper.toSinViajeroResponseDTO(viajero.getPersonNatural()))
                        .build())
                .collect(Collectors.toList());
    }
}
