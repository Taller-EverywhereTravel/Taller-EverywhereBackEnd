package com.everywhere.backend.mapper;

import com.everywhere.backend.model.dto.TravelerFrequentRequestDto;
import com.everywhere.backend.model.dto.TravelerFrequentResponseDto;
import com.everywhere.backend.model.entity.TravelerFrequent;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ViajeroFrecuenteMapper {

    private final ModelMapper modelMapper;

    @PostConstruct
    public void configureMappings() {
        modelMapper.typeMap(TravelerFrequentRequestDto.class, TravelerFrequent.class).addMappings(mapper -> {
            mapper.skip(TravelerFrequent::setTraveler);
        });
    }

    public TravelerFrequent toEntity(TravelerFrequentRequestDto viajeroFrecuenteRequestDto) {
        return modelMapper.map(viajeroFrecuenteRequestDto, TravelerFrequent.class);
    }

    public TravelerFrequentResponseDto toResponse(TravelerFrequent viajeroFrecuente) {
        return modelMapper.map(viajeroFrecuente, TravelerFrequentResponseDto.class);
    }

    public void updateEntityFromDto(TravelerFrequentRequestDto viajeroFrecuenteRequestDto, TravelerFrequent viajeroFrecuente) {
        modelMapper.map(viajeroFrecuenteRequestDto, viajeroFrecuente);
    }
}