package com.everywhere.backend.service;

import com.everywhere.backend.model.dto.PhonePersonRequestDTO;
import com.everywhere.backend.model.dto.PhonePersonResponseDTO;

import java.util.List;
import java.util.Optional;

public interface TelefonoPersonaService {
    List<PhonePersonResponseDTO> findAll();
    Optional<PhonePersonResponseDTO> findById(Integer telefonoId, Integer personaId);
    List<PhonePersonResponseDTO> findByPersonaId(Integer personaId);
    PhonePersonResponseDTO save(PhonePersonRequestDTO telefonoPersonaRequestDTO, Integer personaId);
    PhonePersonResponseDTO update(Integer personaId, PhonePersonRequestDTO TelefonoPersonaRequestDTO, Integer TelefonoPersonaId);
    void deleteById(Integer telefonoId, Integer personaId);
}
