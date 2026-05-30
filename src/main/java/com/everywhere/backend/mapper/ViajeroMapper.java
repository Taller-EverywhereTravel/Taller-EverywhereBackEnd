package com.everywhere.backend.mapper;

import com.everywhere.backend.model.dto.TravelerRequestDTO;
import com.everywhere.backend.model.dto.TravelerResponseDTO;
import com.everywhere.backend.model.entity.Traveler;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ViajeroMapper {

    private final ModelMapper modelMapper;

    public TravelerResponseDTO toResponseDTO(Traveler viajero) {
        return modelMapper.map(viajero, TravelerResponseDTO.class);
    }

    public Traveler toEntity(TravelerRequestDTO viajeroRequestDTO) { 
        return modelMapper.map(viajeroRequestDTO, Traveler.class);
    }

    public void updateEntityFromDTO(TravelerRequestDTO viajeroRequestDTO, Traveler viajero) {
        modelMapper.map(viajeroRequestDTO, viajero); 
    }
}
