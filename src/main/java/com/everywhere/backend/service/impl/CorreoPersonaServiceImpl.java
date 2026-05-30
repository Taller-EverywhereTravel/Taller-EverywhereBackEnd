package com.everywhere.backend.service.impl;

import com.everywhere.backend.mapper.CorreoPersonaMapper;
import com.everywhere.backend.model.dto.MailPersonRequestDTO;
import com.everywhere.backend.model.dto.MailPersonResponseDTO;
import com.everywhere.backend.model.entity.MailPerson;
import com.everywhere.backend.model.entity.Person;
import com.everywhere.backend.repository.CorreoPersonaRepository;
import com.everywhere.backend.repository.PersonaRepository;
import com.everywhere.backend.service.CorreoPersonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CorreoPersonaServiceImpl implements CorreoPersonaService {

    private final CorreoPersonaRepository correoPersonaRepository;
    private final CorreoPersonaMapper correoPersonaMapper;
    private final PersonaRepository personaRepository;

    @Override
    public List<MailPersonResponseDTO> findAll() {
        return correoPersonaRepository.findAll()
                .stream()
                .map(correoPersonaMapper::toResponseDTO)
                .toList();
    }

    @Override
    public Optional<MailPersonResponseDTO> findById(Integer id) {
        return correoPersonaRepository.findById(id)
                .map(correoPersonaMapper::toResponseDTO);
    }

    @Override
    public List<MailPersonResponseDTO> findByPersonaId(Integer personaId) {
        return correoPersonaRepository.findByPersonaId(personaId)
                .stream()
                .map(correoPersonaMapper::toResponseDTO)
                .toList();
    }

    @Override
    public MailPersonResponseDTO save(MailPersonRequestDTO correoPersonaRequestDTO, Integer personaId) {
        Person persona = personaRepository.findById(personaId)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con ID: " + personaId));

        MailPerson correoPersona = correoPersonaMapper.toEntity(correoPersonaRequestDTO);
        correoPersona.setPerson(persona);

        return correoPersonaMapper.toResponseDTO(correoPersonaRepository.save(correoPersona));
    }


    @Override
    public MailPersonResponseDTO update(Integer personaId, MailPersonRequestDTO correoPersonaRequestDTO, Integer correoPersonaId) {
        MailPerson correo = correoPersonaRepository.findById(correoPersonaId)
                .orElseThrow(() -> new RuntimeException("Correo no encontrado con ID: " + correoPersonaId));

        Person persona = personaRepository.findById(personaId)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con ID: " + personaId));

        correoPersonaMapper.updateEntityFromDTO(correo, correoPersonaRequestDTO);
        correo.setPerson(persona);
        return correoPersonaMapper.toResponseDTO(correoPersonaRepository.save(correo));
    }


    @Override
    public void deleteById(Integer id) {
        if (!correoPersonaRepository.existsById(id)) {
            throw new RuntimeException("Correo no encontrado con ID: " + id);
        }
        correoPersonaRepository.deleteById(id);
    }
}
