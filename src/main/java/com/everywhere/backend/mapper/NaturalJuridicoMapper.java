package com.everywhere.backend.mapper;

import com.everywhere.backend.model.dto.NaturalJuridicResponseDTO;
import com.everywhere.backend.model.entity.NaturalJuridic;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NaturalJuridicoMapper {

    private final ModelMapper modelMapper;
    private final PersonaNaturalMapper personaNaturalMapper;
    private final PersonaJuridicaMapper personaJuridicaMapper;

    public NaturalJuridicResponseDTO toResponseDTO(NaturalJuridic naturalJuridico) {
        NaturalJuridicResponseDTO dto = modelMapper.map(naturalJuridico, NaturalJuridicResponseDTO.class);
        if (naturalJuridico.getPersonNatural() != null)
            dto.setPersonNatural(personaNaturalMapper.toResponseDTO(naturalJuridico.getPersonNatural()));
        if (naturalJuridico.getPersonJuridic() != null)
            dto.setPersonJuridic(personaJuridicaMapper.toResponseDTO(naturalJuridico.getPersonJuridic()));
        return dto;
    }
}