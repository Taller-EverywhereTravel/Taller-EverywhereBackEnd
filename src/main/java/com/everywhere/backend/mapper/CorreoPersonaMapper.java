package com.everywhere.backend.mapper;

import com.everywhere.backend.model.dto.MailPersonRequestDTO;
import com.everywhere.backend.model.dto.MailPersonResponseDTO;
import com.everywhere.backend.model.entity.MailPerson;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CorreoPersonaMapper {

    private final ModelMapper modelMapper;

    public MailPerson toEntity(MailPersonRequestDTO correoPersona) {
        return modelMapper.map(correoPersona, MailPerson.class);
    }

    public MailPersonResponseDTO toResponseDTO(MailPerson correoPersona) {
        return modelMapper.map(correoPersona, MailPersonResponseDTO.class);
    }

    public void updateEntityFromDTO(MailPerson correoPersona, MailPersonRequestDTO correoPersonaRequestDTO) {
        modelMapper.map(correoPersonaRequestDTO, correoPersona);
    }
}
