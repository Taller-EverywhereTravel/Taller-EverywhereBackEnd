package com.everywhere.backend.service;

import com.everywhere.backend.model.dto.PersonJuridicRequestDTO;
import com.everywhere.backend.model.dto.PersonJuridicResponseDTO;

import java.util.List;

public interface PersonaJuridicaService {
    List<PersonJuridicResponseDTO> findAll();
    PersonJuridicResponseDTO findById(Integer id);
    List<PersonJuridicResponseDTO> findByRuc(String ruc);
    List<PersonJuridicResponseDTO> findByRazonSocial(String razonSocial);
    PersonJuridicResponseDTO save(PersonJuridicRequestDTO personaJuridicaRequestDTO);
    PersonJuridicResponseDTO patch(Integer id, PersonJuridicRequestDTO personaJuridicaRequestDTO);
    void deleteById(Integer id);
}
