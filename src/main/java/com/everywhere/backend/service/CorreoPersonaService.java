package com.everywhere.backend.service;

import com.everywhere.backend.model.dto.MailPersonRequestDTO;
import com.everywhere.backend.model.dto.MailPersonResponseDTO;

import java.util.List;
import java.util.Optional;

public interface CorreoPersonaService {

    List<MailPersonResponseDTO> findAll();
    Optional<MailPersonResponseDTO> findById(Integer id);
    List<MailPersonResponseDTO> findByPersonaId(Integer personaId);
    MailPersonResponseDTO save(MailPersonRequestDTO correoPersonaRequestDTO, Integer personaId);
    MailPersonResponseDTO update(Integer personaId, MailPersonRequestDTO correoPersonaRequestDTO, Integer correoPersonaId);
    void deleteById(Integer id);
}
