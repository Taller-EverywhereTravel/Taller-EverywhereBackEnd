package com.everywhere.backend.service;

import com.everywhere.backend.model.dto.PersonRequestDTO;
import com.everywhere.backend.model.dto.PersonResponseDTO;
import com.everywhere.backend.model.dto.PersonDisplayDto;

import java.util.List;

public interface PersonaService {
    List<PersonResponseDTO> findAll();
    PersonResponseDTO findById(Integer id);
    List<PersonResponseDTO> findByEmail(String email);
    List<PersonResponseDTO> findByTelefono(String telefono);
    PersonResponseDTO save(PersonRequestDTO personaRequestDTO);
    PersonResponseDTO patch(Integer id, PersonRequestDTO personaRequestDTO);
    void deleteById(Integer id);
    PersonDisplayDto findPersonaNaturalOrJuridicaById(Integer id);
}
