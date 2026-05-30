package com.everywhere.backend.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.everywhere.backend.model.dto.*;
import com.everywhere.backend.model.entity.Quotation;
import lombok.RequiredArgsConstructor;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CotizacionMapper {

    private final ModelMapper modelMapper;

    @PostConstruct
    public void configureMapping() {
        modelMapper.typeMap(QuotationRequestDto.class, Quotation.class).addMappings(mapper -> {
            mapper.skip(Quotation::setCounter);
            mapper.skip(Quotation::setMethodPayment);
            mapper.skip(Quotation::setStatusQuotation);
            mapper.skip(Quotation::setBranch);
            mapper.skip(Quotation::setFolder);
        });
    }

    public QuotationResponseDto toResponse(Quotation cotizacion) { 
        return modelMapper.map(cotizacion, QuotationResponseDto.class);
    }

    public Quotation toEntity(QuotationRequestDto cotizacionRequestDto) { 
        return modelMapper.map(cotizacionRequestDto, Quotation.class);
    }
    
    public void updateEntityFromRequest(Quotation cotizacion, QuotationRequestDto cotizacionRequestDto) {
        if (cotizacionRequestDto.getNameQuotation() != null) {
            cotizacion.setNameQuotation(cotizacionRequestDto.getNameQuotation());
        }
        if (cotizacionRequestDto.getNumAdult() != null) {
            cotizacion.setNumAdult(cotizacionRequestDto.getNumAdult());
        }
        if (cotizacionRequestDto.getNumChild() != null) {
            cotizacion.setNumChild(cotizacionRequestDto.getNumChild());
        }
        if (cotizacionRequestDto.getDateExpiration() != null) {
            cotizacion.setDateExpiration(cotizacionRequestDto.getDateExpiration());
        }
        if (cotizacionRequestDto.getOriginDestination() != null) {
            cotizacion.setOriginDestination(cotizacionRequestDto.getOriginDestination());
        }
        if (cotizacionRequestDto.getDateDeparture() != null) {
            cotizacion.setDateDeparture(cotizacionRequestDto.getDateDeparture());
        }
        if (cotizacionRequestDto.getDateReturn() != null) {
            cotizacion.setDateReturn(cotizacionRequestDto.getDateReturn());
        }
        if (cotizacionRequestDto.getCurrency() != null) {
            cotizacion.setCurrency(cotizacionRequestDto.getCurrency());
        }
        if (cotizacionRequestDto.getObservation() != null) {
            cotizacion.setObservation(cotizacionRequestDto.getObservation());
        }
    }

    public QuotationWithDetailResponseDTO toResponseWithDetalles(QuotationResponseDto cotizacionResponseDto, 
        List<DetailQuotationSimpleDTO> detalleCotizacionSimpleDTOs) {
        QuotationWithDetailResponseDTO cotizacionConDetallesResponseDTO = modelMapper.map(cotizacionResponseDto, QuotationWithDetailResponseDTO.class);
        cotizacionConDetallesResponseDTO.setDetail(detalleCotizacionSimpleDTOs);
        return cotizacionConDetallesResponseDTO;
    }

    public DetailQuotationSimpleDTO toDetalleSimple(DetailQuotationResponseDto detalleCotizacionResponseDto) {
        return modelMapper.map(detalleCotizacionResponseDto, DetailQuotationSimpleDTO.class);
    }
}