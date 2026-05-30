package com.everywhere.backend.mapper;

import com.everywhere.backend.model.dto.ObservationLiquidationRequestDTO;
import com.everywhere.backend.model.dto.ObservationLiquidationResponseDTO;
import com.everywhere.backend.model.entity.ObservationLiquidation; 
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ObservacionLiquidacionMapper {

    private final ModelMapper modelMapper; 

    @PostConstruct
    public void configureMapping() {
        modelMapper.typeMap(ObservationLiquidationRequestDTO.class, ObservationLiquidation.class)
                .addMappings(mapper -> mapper.skip(ObservationLiquidation::setLiquidation));
    }

    public ObservationLiquidation toEntity(ObservationLiquidationRequestDTO observacionLiquidacionRequestDTO) {
        ObservationLiquidation observacionLiquidacion =
                modelMapper.map(observacionLiquidacionRequestDTO, ObservationLiquidation.class);

        return observacionLiquidacion;
    }

    public ObservationLiquidationResponseDTO toResponseDTO(ObservationLiquidation observacionLiquidacion) {
        ObservationLiquidationResponseDTO observacionLiquidacionResponseDTO =
                modelMapper.map(observacionLiquidacion, ObservationLiquidationResponseDTO.class);

        return observacionLiquidacionResponseDTO;
    }

    public void updateEntityFromDTO(ObservationLiquidationRequestDTO observacionLiquidacionRequestDTO,
                                    ObservationLiquidation observacionLiquidacion) {
        modelMapper.map(observacionLiquidacionRequestDTO, observacionLiquidacion);
   }
}