package com.everywhere.backend.mapper;

import com.everywhere.backend.model.dto.LiquidationRequestDTO;
import com.everywhere.backend.model.dto.LiquidationResponseDTO;
import com.everywhere.backend.model.entity.Liquidation;

import jakarta.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LiquidacionMapper {

    private final ModelMapper modelMapper;

    @PostConstruct
    public void configureMapping() {
        modelMapper.typeMap(LiquidationRequestDTO.class, Liquidation.class).addMappings(mapper -> {
            mapper.skip(Liquidation::setQuotation);
            mapper.skip(Liquidation::setProduct);
            mapper.skip(Liquidation::setMethodPayment);
            mapper.skip(Liquidation::setFolder);
        });
    }

    public LiquidationResponseDTO toResponseDTO(Liquidation liquidacion) {
        LiquidationResponseDTO liquidacionResponseDTO = modelMapper.map(liquidacion, LiquidationResponseDTO.class);
        return liquidacionResponseDTO;
    }

    public Liquidation toEntity(LiquidationRequestDTO liquidacionRequestDTO) {
        Liquidation liquidacion = modelMapper.map(liquidacionRequestDTO, Liquidation.class);
        return liquidacion;
    }

    public void updateEntityFromRequest(Liquidation liquidacion, LiquidationRequestDTO liquidacionRequestDTO) {
        modelMapper.map(liquidacionRequestDTO, liquidacion);
    }
}