package com.everywhere.backend.mapper;

import com.everywhere.backend.model.dto.StatusQuotationRequestDTO;
import com.everywhere.backend.model.dto.StatusQuotationResponseDTO;
import com.everywhere.backend.model.entity.StatusQuotation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EstadoCotizacionMapper {

    @Autowired
    private ModelMapper modelMapper;
    public StatusQuotation toEntity(StatusQuotationRequestDTO estadoCotizacionRequestDTO) {
        return modelMapper.map(estadoCotizacionRequestDTO, StatusQuotation.class);
    }
        public StatusQuotationResponseDTO toResponseDTO(StatusQuotation estadoCotizacion) {
        return modelMapper.map(estadoCotizacion, StatusQuotationResponseDTO.class);
    }
        public void updateEntityFromDTO(StatusQuotationRequestDTO estadoCotizacionRequestDTO, StatusQuotation estadoCotizacion) {
        modelMapper.map(estadoCotizacionRequestDTO, estadoCotizacion);
    }
}
