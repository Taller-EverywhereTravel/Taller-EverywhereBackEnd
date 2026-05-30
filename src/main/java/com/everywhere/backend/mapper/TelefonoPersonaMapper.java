package com.everywhere.backend.mapper;

import com.everywhere.backend.model.dto.PhonePersonRequestDTO;
import com.everywhere.backend.model.dto.PhonePersonResponseDTO;
import com.everywhere.backend.model.entity.PhonePerson;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelefonoPersonaMapper {

    private final ModelMapper modelMapper;

    public PhonePerson toEntity(PhonePersonRequestDTO telefonoPersonaRequestDTO) {
        return modelMapper.map(telefonoPersonaRequestDTO, PhonePerson.class);
    }

    public PhonePersonResponseDTO toResponseDTO(PhonePerson telefonoPersona) {
        return modelMapper.map(telefonoPersona, PhonePersonResponseDTO.class);
    }

    public void updateEntityFromDTO(PhonePersonRequestDTO telefonoPersonaRequestDTO, PhonePerson telefonoPersona) {
        modelMapper.map(telefonoPersonaRequestDTO, telefonoPersona);
    }
}
