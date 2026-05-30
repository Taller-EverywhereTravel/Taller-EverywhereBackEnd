package com.everywhere.backend.service.impl;

import com.everywhere.backend.model.dto.NaturalJuridicRequestDTO;
import com.everywhere.backend.model.dto.NaturalJuridicResponseDTO;
import com.everywhere.backend.model.dto.NaturalJuridicPatchDTO;
import com.everywhere.backend.model.entity.NaturalJuridic;
import com.everywhere.backend.model.entity.PersonNatural;
import com.everywhere.backend.model.entity.PersonJuridic;
import com.everywhere.backend.repository.NaturalJuridicoRepository;
import com.everywhere.backend.repository.PersonaNaturalRepository;
import com.everywhere.backend.repository.PersonaJuridicaRepository;
import com.everywhere.backend.service.NaturalJuridicoService;

import com.everywhere.backend.exceptions.ResourceNotFoundException; 
import com.everywhere.backend.mapper.NaturalJuridicoMapper;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NaturalJuridicoServiceImpl implements NaturalJuridicoService {

    private final NaturalJuridicoRepository naturalJuridicoRepository;
    private final PersonaNaturalRepository personaNaturalRepository;
    private final PersonaJuridicaRepository personaJuridicaRepository;
    private final NaturalJuridicoMapper naturalJuridicoMapper;

    @Override
    @Transactional
    public List<NaturalJuridicResponseDTO> crearRelaciones(NaturalJuridicRequestDTO naturalJuridicoRequestDTO) {
        if (!personaNaturalRepository.existsById(naturalJuridicoRequestDTO.getPersonNaturalId()))
            throw new ResourceNotFoundException("Persona natural no encontrada con ID: " + naturalJuridicoRequestDTO.getPersonNaturalId());

        for (Integer personaJuridicaId : naturalJuridicoRequestDTO.getPersonJuridicIds()) {
            if (!personaJuridicaRepository.existsById(personaJuridicaId))
                throw new ResourceNotFoundException("Persona jurídica no encontrada con ID: " + personaJuridicaId);
                
            Optional<NaturalJuridic> naturalJuridicOptional = naturalJuridicoRepository
                    .findByPersonNaturalIdAndPersonJuridicId(naturalJuridicoRequestDTO.getPersonNaturalId(), personaJuridicaId);

            if (naturalJuridicOptional.isPresent())
                throw new DataIntegrityViolationException("Ya existe una relación entre la persona natural " + 
                    naturalJuridicoRequestDTO.getPersonNaturalId() + " y la persona jurídica " + personaJuridicaId);
        }

        PersonNatural personaNatural = personaNaturalRepository.findById(naturalJuridicoRequestDTO.getPersonNaturalId()).get();
        List<NaturalJuridic> naturalJuridicoList = new ArrayList<>();

        for (Integer personaJuridicaId : naturalJuridicoRequestDTO.getPersonJuridicIds()) {
            PersonJuridic personaJuridica = personaJuridicaRepository.findById(personaJuridicaId).get();

            NaturalJuridic nuevaRelacion = new NaturalJuridic();
            nuevaRelacion.setPersonNatural(personaNatural);
            nuevaRelacion.setPersonJuridic(personaJuridica);

            naturalJuridicoList.add(naturalJuridicoRepository.save(nuevaRelacion));
        }
        return mapToResponseList(naturalJuridicoList);
    }

    @Override
    public List<NaturalJuridicResponseDTO> findByPersonaNaturalId(Integer personaNaturalId) {
        if (!personaNaturalRepository.existsById(personaNaturalId))
            throw new ResourceNotFoundException("Persona natural no encontrada con ID: " + personaNaturalId); 
        return mapToResponseList(naturalJuridicoRepository.findByPersonNaturalId(personaNaturalId));
    }

    @Override
    public List<NaturalJuridicResponseDTO> findByPersonaJuridicaId(Integer personaJuridicaId) {
        if (!personaJuridicaRepository.existsById(personaJuridicaId))
            throw new ResourceNotFoundException("Persona jurídica no encontrada con ID: " + personaJuridicaId);
        return mapToResponseList(naturalJuridicoRepository.findByPersonJuridicId(personaJuridicaId));
    }

    @Override
    public NaturalJuridicResponseDTO findById(Integer id) {
        NaturalJuridic naturalJuridico = naturalJuridicoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Relación no encontrada con ID: " + id));
        return naturalJuridicoMapper.toResponseDTO(naturalJuridico);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        if (!naturalJuridicoRepository.existsById(id)) 
            throw new ResourceNotFoundException("Relación no encontrada con ID: " + id);
        naturalJuridicoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByPersonas(Integer personaNaturalId, Integer personaJuridicaId) {
        Optional<NaturalJuridic> naturalJuridicoOptional = naturalJuridicoRepository.findByPersonNaturalIdAndPersonJuridicId(personaNaturalId, personaJuridicaId);
        if (naturalJuridicoOptional.isEmpty())
            throw new ResourceNotFoundException("No existe relación entre la persona natural " + personaNaturalId + " y la persona jurídica " + personaJuridicaId);      
        naturalJuridicoRepository.deleteByPersonNaturalIdAndPersonJuridicId(personaNaturalId, personaJuridicaId);
    }

    @Override
    public List<NaturalJuridicResponseDTO> findAll() { 
        return mapToResponseList(naturalJuridicoRepository.findAll());
    }

    @Override
    @Transactional
    public List<NaturalJuridicResponseDTO> patchRelacionesPersonaNatural(Integer personaNaturalId, NaturalJuridicPatchDTO naturalJuridicoPatchDTO) {
        if (!personaNaturalRepository.existsById(personaNaturalId))
            throw new ResourceNotFoundException("Persona natural no encontrada con ID: " + personaNaturalId);

        if (naturalJuridicoPatchDTO.getAdd() != null && !naturalJuridicoPatchDTO.getAdd().isEmpty()) {
            for (Integer personaJuridicaId : naturalJuridicoPatchDTO.getAdd()) {
                if (!personaJuridicaRepository.existsById(personaJuridicaId))
                    throw new ResourceNotFoundException("Persona jurídica no encontrada con ID: " + personaJuridicaId);
            }
        }

        PersonNatural personaNatural = null;

        if (naturalJuridicoPatchDTO.getRemove() != null && !naturalJuridicoPatchDTO.getRemove().isEmpty()) {
            for (Integer personaJuridicaId : naturalJuridicoPatchDTO.getRemove()) {
                Optional<NaturalJuridic> relacionExistente = naturalJuridicoRepository
                        .findByPersonNaturalIdAndPersonJuridicId(personaNaturalId, personaJuridicaId);

                if (relacionExistente.isPresent()) naturalJuridicoRepository.deleteById(relacionExistente.get().getId());
            }
        }

        if (naturalJuridicoPatchDTO.getAdd() != null && !naturalJuridicoPatchDTO.getAdd().isEmpty()) {
            if (personaNatural == null)
                personaNatural = personaNaturalRepository.findById(personaNaturalId).get();
            
            for (Integer personaJuridicaId : naturalJuridicoPatchDTO.getAdd()) {
                Optional<NaturalJuridic> relacionExistente = naturalJuridicoRepository
                        .findByPersonNaturalIdAndPersonJuridicId(personaNaturalId, personaJuridicaId);
                
                if (relacionExistente.isEmpty()) {
                    PersonJuridic personaJuridica = personaJuridicaRepository.findById(personaJuridicaId).get();
                    
                    NaturalJuridic nuevaRelacion = new NaturalJuridic();
                    nuevaRelacion.setPersonNatural(personaNatural);
                    nuevaRelacion.setPersonJuridic(personaJuridica);
                    naturalJuridicoRepository.save(nuevaRelacion);
                }
            }
        }
        return findByPersonaNaturalId(personaNaturalId);
    }

    private List<NaturalJuridicResponseDTO> mapToResponseList(List<NaturalJuridic> naturalJuridicos) {
        return naturalJuridicos.stream().map(naturalJuridicoMapper::toResponseDTO).toList();
    }
}