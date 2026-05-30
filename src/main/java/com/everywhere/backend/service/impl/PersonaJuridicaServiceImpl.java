package com.everywhere.backend.service.impl;
 
import com.everywhere.backend.model.dto.PersonJuridicRequestDTO;
import com.everywhere.backend.model.dto.PersonJuridicResponseDTO;
import com.everywhere.backend.model.entity.PersonJuridic;
import com.everywhere.backend.model.entity.Person;
import com.everywhere.backend.repository.PersonaJuridicaRepository;
import com.everywhere.backend.repository.PersonaRepository;
import com.everywhere.backend.service.PersonaJuridicaService; 

import com.everywhere.backend.exceptions.ResourceNotFoundException;
import com.everywhere.backend.mapper.PersonaJuridicaMapper;
import com.everywhere.backend.mapper.PersonaMapper;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor; 
import java.util.List;
import java.util.Optional; 

@Service
@RequiredArgsConstructor
public class PersonaJuridicaServiceImpl implements PersonaJuridicaService {

    private final PersonaJuridicaRepository personaJuridicaRepository;
    private final PersonaRepository personaRepository;
    private final PersonaJuridicaMapper personaJuridicaMapper;
    private final PersonaMapper personaMapper;

    @Override
    public List<PersonJuridicResponseDTO> findAll() {
        return personaJuridicaRepository.findAll().stream().map(personaJuridicaMapper::toResponseDTO).toList();
    }

    @Override
    public PersonJuridicResponseDTO findById(Integer id) {
        PersonJuridic personaJuridica = personaJuridicaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Persona jurídica no encontrada con ID: " + id));
        return personaJuridicaMapper.toResponseDTO(personaJuridica);
    }

    @Override
    public List<PersonJuridicResponseDTO> findByRuc(String ruc) {
        Optional<PersonJuridic> personaJuridOptional = personaJuridicaRepository.findByRucIgnoreCase(ruc);
        if (personaJuridOptional.isEmpty())
            throw new ResourceNotFoundException("No se encontró persona jurídica con RUC: " + ruc);
        return List.of(personaJuridicaMapper.toResponseDTO(personaJuridOptional.get()));
    }

    @Override
    public List<PersonJuridicResponseDTO> findByRazonSocial(String razonSocial) {
        List<PersonJuridic> personaJuridicaList = personaJuridicaRepository.findByRazonSocialIgnoreAccents(razonSocial);
        if (personaJuridicaList.isEmpty())
            throw new ResourceNotFoundException("No se encontraron personas jurídicas con razón social: " + razonSocial);
        return personaJuridicaList.stream().map(personaJuridicaMapper::toResponseDTO).toList();
    }

    @Override
    public PersonJuridicResponseDTO save(PersonJuridicRequestDTO personaJuridicaRequestDTO) {
        // Validar que no exista ya una persona con el mismo RUC
        if (personaJuridicaRequestDTO.getRuc() != null && !personaJuridicaRequestDTO.getRuc().trim().isEmpty()) { 
            if (personaJuridicaRepository.findByRucIgnoreCase(personaJuridicaRequestDTO.getRuc().trim()).isPresent())
                throw new DataIntegrityViolationException("Ya existe una persona jurídica con el RUC: " + personaJuridicaRequestDTO.getRuc());
        }

        // Crear la persona base
        Person persona = (personaJuridicaRequestDTO.getPerson() != null) 
            ? personaMapper.toEntity(personaJuridicaRequestDTO.getPerson())
            : new Person(); 

        // Crear la persona jurídica
        PersonJuridic personaJuridica = personaJuridicaMapper.toEntity(personaJuridicaRequestDTO);
        personaJuridica.setPerson(personaRepository.save(persona)); 

        return personaJuridicaMapper.toResponseDTO(personaJuridicaRepository.save(personaJuridica));
    }

    @Override
    public PersonJuridicResponseDTO patch(Integer id, PersonJuridicRequestDTO personaJuridicaRequestDTO) {
        // 🚀 OPTIMIZACIÓN 1: Validar existencia ANTES de buscar el objeto
        if (!personaJuridicaRepository.existsById(id))
            throw new ResourceNotFoundException("Persona jurídica no encontrada con ID: " + id);

        // 🚀 OPTIMIZACIÓN 2: Si viene RUC, validar duplicado ANTES de buscar el objeto completo
        if (personaJuridicaRequestDTO.getRuc() != null && !personaJuridicaRequestDTO.getRuc().trim().isEmpty()) { 
            String newRuc = personaJuridicaRequestDTO.getRuc().trim();
            if (personaJuridicaRepository.findByRucIgnoreCaseAndIdNot(newRuc, id).isPresent()) {
                throw new DataIntegrityViolationException("Ya existe otra persona jurídica con el RUC: " + personaJuridicaRequestDTO.getRuc());
            }
        }

        // Solo ahora buscar el objeto para hacer el update
        PersonJuridic existingPersonaJuridica = personaJuridicaRepository.findById(id).get();
        personaJuridicaMapper.updateEntityFromDTO(personaJuridicaRequestDTO, existingPersonaJuridica); 
        return personaJuridicaMapper.toResponseDTO(personaJuridicaRepository.save(existingPersonaJuridica));
    }

    @Override
    public void deleteById(Integer id) {
        if (!personaJuridicaRepository.existsById(id))
            throw new ResourceNotFoundException("Persona jurídica no encontrada con ID: " + id);
        personaJuridicaRepository.deleteById(id);
    }
}