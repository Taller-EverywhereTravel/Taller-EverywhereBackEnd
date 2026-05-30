package com.everywhere.backend.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.everywhere.backend.model.dto.DetailQuotationRequestDto;
import com.everywhere.backend.model.dto.DetailQuotationResponseDto;
import com.everywhere.backend.model.entity.DetailQuotation;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DetalleCotizacionMapper {

    private final ModelMapper modelMapper;

    @PostConstruct
    public void configureMappings() {
        modelMapper.typeMap(DetailQuotationRequestDto.class, DetailQuotation.class).addMappings(mapper -> {
                mapper.skip(DetailQuotation::setQuotation);
                mapper.skip(DetailQuotation::setProduct);
                mapper.skip(DetailQuotation::setSupplier);
                mapper.skip(DetailQuotation::setOperator);
            });
    }

    public DetailQuotationResponseDto toResponse(DetailQuotation detalleCotizacion) {
        DetailQuotationResponseDto detalleCotizacionResponseDto = modelMapper.map(detalleCotizacion, DetailQuotationResponseDto.class);
        return detalleCotizacionResponseDto;
    }

    public DetailQuotation toEntity(DetailQuotationRequestDto detalleCotizacionRequestDto) {
        DetailQuotation detalleCotizacion = modelMapper.map(detalleCotizacionRequestDto, DetailQuotation.class);
        return detalleCotizacion;
    }

    public void updateEntityFromRequest(DetailQuotation detalleCotizacion, DetailQuotationRequestDto detalleCotizacionRequestDto) {
        if (detalleCotizacionRequestDto.getQuantity() != null) {
            detalleCotizacion.setQuantity(detalleCotizacionRequestDto.getQuantity());
        }
        if (detalleCotizacionRequestDto.getUnit() != null) {
            detalleCotizacion.setUnit(detalleCotizacionRequestDto.getUnit());
        }
        if (detalleCotizacionRequestDto.getDescription() != null) {
            detalleCotizacion.setDescription(detalleCotizacionRequestDto.getDescription());
        }
        if (detalleCotizacionRequestDto.getCommission() != null) {
            detalleCotizacion.setCommission(detalleCotizacionRequestDto.getCommission());
        }
        if (detalleCotizacionRequestDto.getPriceHistory() != null) {
            detalleCotizacion.setPriceHistory(detalleCotizacionRequestDto.getPriceHistory());
        }
        if (detalleCotizacionRequestDto.getSelected() != null) {
            detalleCotizacion.setSelected(detalleCotizacionRequestDto.getSelected());
        }
    }
}