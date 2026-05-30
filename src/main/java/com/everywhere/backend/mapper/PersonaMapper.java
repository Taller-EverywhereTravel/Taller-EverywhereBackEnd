package com.everywhere.backend.mapper;

import com.everywhere.backend.model.dto.PersonRequestDTO;
import com.everywhere.backend.model.dto.PersonResponseDTO;
import com.everywhere.backend.model.dto.PersonDisplayDto;
import com.everywhere.backend.model.entity.PersonJuridic;
import com.everywhere.backend.model.entity.PersonNatural;
import com.everywhere.backend.model.entity.Person;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PersonaMapper {

    private final ModelMapper modelMapper;
    private final TelefonoPersonaMapper telefonoPersonaMapper;

    public PersonResponseDTO toResponseDTO(Person persona) {
        PersonResponseDTO personaResponseDTO = modelMapper.map(persona, PersonResponseDTO.class);

        if (persona.getPhone() != null) {
            personaResponseDTO.setPhone(
                    persona.getPhone().stream()
                            .map(telefonoPersonaMapper::toResponseDTO)
                            .collect(Collectors.toList())
            );
        }

        return personaResponseDTO;
    }

    public Person toEntity(PersonRequestDTO personaRequestDTO) {
        return modelMapper.map(personaRequestDTO, Person.class);
    }

    public void updateEntityFromDTO(PersonRequestDTO personaRequestDTO, Person personas) {
        modelMapper.map(personaRequestDTO, personas);
    }

    public PersonDisplayDto toDisplayDTO(PersonNatural personaNatural) {
        String nombreCompleto = personaNatural.getName() + " " +
                personaNatural.getSurnamePaternal() + " " +
                personaNatural.getSurnameMaternal();
        return new PersonDisplayDto(
                personaNatural.getId(),
                "NATURAL",
                String.valueOf(personaNatural.getDocument()),
                nombreCompleto
        );
    }

    public PersonDisplayDto toDisplayDTO(PersonJuridic personaJuridica) {
        return new PersonDisplayDto(
                personaJuridica.getId(),
                "JURIDICA",
                String.valueOf(personaJuridica.getRuc()),
                personaJuridica.getNameCompany()
        );
    }
}
