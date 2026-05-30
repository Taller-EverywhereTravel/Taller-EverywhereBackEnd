package com.everywhere.backend.mapper;

import com.everywhere.backend.model.dto.PersonJuridicRequestDTO;
import com.everywhere.backend.model.dto.PersonJuridicResponseDTO;
import com.everywhere.backend.model.entity.PersonJuridic;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PersonaJuridicaMapper {

    private final ModelMapper modelMapper;
    private final PersonaMapper personaMapper;

    public PersonJuridicResponseDTO toResponseDTO(PersonJuridic personaJuridica) {
        PersonJuridicResponseDTO personaJuridicaResponseDTO = modelMapper.map(personaJuridica, PersonJuridicResponseDTO.class);
        if (personaJuridica.getPerson() != null) {
            personaJuridicaResponseDTO.setPerson(personaMapper.toResponseDTO(personaJuridica.getPerson()));
        }
        return personaJuridicaResponseDTO;
    }

    public PersonJuridic toEntity(PersonJuridicRequestDTO personaJuridicaRequestDTO) {
        return modelMapper.map(personaJuridicaRequestDTO, PersonJuridic.class);
    }

    public void updateEntityFromDTO(PersonJuridicRequestDTO personaJuridicaRequestDTO, PersonJuridic personaJuridica) {
        modelMapper.map(personaJuridicaRequestDTO, personaJuridica);

        // Actualizar persona base si existe
        if (personaJuridicaRequestDTO.getPerson() != null && personaJuridica.getPerson() != null) {
            personaMapper.updateEntityFromDTO(personaJuridicaRequestDTO.getPerson(), personaJuridica.getPerson());
        }
    }
}
