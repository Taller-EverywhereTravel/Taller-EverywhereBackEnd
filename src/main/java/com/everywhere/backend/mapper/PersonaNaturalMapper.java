package com.everywhere.backend.mapper;

import com.everywhere.backend.model.dto.PersonNaturalRequestDTO;
import com.everywhere.backend.model.dto.PersonNaturalResponseDTO;
import com.everywhere.backend.model.dto.PersonNaturalWithoutTravelerResponseDTO;
import com.everywhere.backend.model.entity.PersonNatural;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PersonaNaturalMapper {

    private final ModelMapper modelMapper;
    private final PersonaMapper personaMapper;
    private final ViajeroMapper viajeroMapper;
    private final CategoriaPersonaMapper categoriaPersonaMapper; // ✅ Agregado

    public PersonNaturalResponseDTO toResponseDTO(PersonNatural personaNatural) {
        PersonNaturalResponseDTO personaNaturalResponseDTO = modelMapper.map(personaNatural, PersonNaturalResponseDTO.class);

        // ✅ Corregido: getPersonas() en lugar de getPersona()
        if (personaNatural.getPerson() != null) {
            personaNaturalResponseDTO.setPerson(personaMapper.toResponseDTO(personaNatural.getPerson()));
        }

        if (personaNatural.getTraveler() != null) {
            personaNaturalResponseDTO.setTraveler(viajeroMapper.toResponseDTO(personaNatural.getTraveler()));
        }

        return personaNaturalResponseDTO;
    }

    public PersonNatural toEntity(PersonNaturalRequestDTO personaNaturalRequestDTOdto) {
        return modelMapper.map(personaNaturalRequestDTOdto, PersonNatural.class);
    }

    public void updateEntityFromDTO(PersonNaturalRequestDTO personaNaturalRequestDTO, PersonNatural personaNatural) {
        modelMapper.map(personaNaturalRequestDTO, personaNatural);

        if (personaNaturalRequestDTO.getPerson() != null && personaNatural.getPerson() != null)
            personaMapper.updateEntityFromDTO(personaNaturalRequestDTO.getPerson(), personaNatural.getPerson());
    }

    public PersonNaturalWithoutTravelerResponseDTO toSinViajeroResponseDTO(PersonNatural personaNatural) {
        PersonNaturalWithoutTravelerResponseDTO dto = modelMapper.map(personaNatural, PersonNaturalWithoutTravelerResponseDTO.class);

        if (personaNatural.getPerson() != null) {
            dto.setPerson(personaMapper.toResponseDTO(personaNatural.getPerson()));
        }

        if (personaNatural.getCategoryPerson() != null) {
            dto.setCategoryPerson(categoriaPersonaMapper.toResponseDTO(personaNatural.getCategoryPerson()));
        }

        return dto;
    }
}