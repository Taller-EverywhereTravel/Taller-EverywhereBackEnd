package com.everywhere.backend.mapper;

import com.everywhere.backend.model.dto.DetailLiquidationRequestDTO;
import com.everywhere.backend.model.dto.DetailLiquidationResponseDTO;
import com.everywhere.backend.model.dto.DetailLiquidationWithoutLiquidationDTO;
import com.everywhere.backend.model.entity.DetailLiquidation;

import jakarta.annotation.PostConstruct;

import org.modelmapper.ModelMapper; 
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DetalleLiquidacionMapper {

    private final ModelMapper modelMapper;

    @PostConstruct
    public void init() {
        modelMapper.typeMap(DetailLiquidationRequestDTO.class, DetailLiquidation.class)
                .addMappings(mapper -> mapper.skip(DetailLiquidation::setId));
    }

    public DetailLiquidationResponseDTO toResponseDTO(DetailLiquidation detalleLiquidacion) {
        DetailLiquidationResponseDTO detalleLiquidacionResponseDTO = modelMapper.map(detalleLiquidacion, DetailLiquidationResponseDTO.class);
        return detalleLiquidacionResponseDTO;
    }

    public DetailLiquidationWithoutLiquidationDTO toSinLiquidacionDTO(DetailLiquidation detalleLiquidacion) {
        DetailLiquidationWithoutLiquidationDTO detalleLiquidacionSinLiquidacionDTO = modelMapper.map(detalleLiquidacion, DetailLiquidationWithoutLiquidationDTO.class);
        return detalleLiquidacionSinLiquidacionDTO;
    }

    public DetailLiquidation toEntity(DetailLiquidationRequestDTO detalleLiquidacionRequestDTO) {
        DetailLiquidation detalleLiquidacion = new DetailLiquidation();
        updateEntityFromDTO(detalleLiquidacionRequestDTO, detalleLiquidacion);
        return detalleLiquidacion;
    }

    public void updateEntityFromDTO(DetailLiquidationRequestDTO detalleLiquidacionRequestDTO, DetailLiquidation detalleLiquidacion) {
        if (detalleLiquidacionRequestDTO.getTicket() != null) {
            detalleLiquidacion.setTicket(detalleLiquidacionRequestDTO.getTicket());
        }
        if (detalleLiquidacionRequestDTO.getDocumentCollection() != null) {
            detalleLiquidacion.setDocumentCollection(detalleLiquidacionRequestDTO.getDocumentCollection());
        }
        if (detalleLiquidacionRequestDTO.getCostTicket() != null) {
            detalleLiquidacion.setCostTicket(detalleLiquidacionRequestDTO.getCostTicket());
        }
        if (detalleLiquidacionRequestDTO.getChargeService() != null) {
            detalleLiquidacion.setChargeService(detalleLiquidacionRequestDTO.getChargeService());
        }
        if (detalleLiquidacionRequestDTO.getValueSale() != null) {
            detalleLiquidacion.setValueSale(detalleLiquidacionRequestDTO.getValueSale());
        }
        if (detalleLiquidacionRequestDTO.getFeeEmision() != null) {
            detalleLiquidacion.setFeeEmision(detalleLiquidacionRequestDTO.getFeeEmision());
        }
        if (detalleLiquidacionRequestDTO.getDocumentFee() != null) {
            detalleLiquidacion.setDocumentFee(detalleLiquidacionRequestDTO.getDocumentFee());
        }
        if (detalleLiquidacionRequestDTO.getComission() != null) {
            detalleLiquidacion.setCommission(detalleLiquidacionRequestDTO.getComission());
        }
        if (detalleLiquidacionRequestDTO.getInvoicePurchase() != null) {
            detalleLiquidacion.setInvoicePurchase(detalleLiquidacionRequestDTO.getInvoicePurchase());
        }
        if (detalleLiquidacionRequestDTO.getTicketPassenger() != null) {
            detalleLiquidacion.setTicketPassenger(detalleLiquidacionRequestDTO.getTicketPassenger());
        }
        if (detalleLiquidacionRequestDTO.getAmountDiscount() != null) {
            detalleLiquidacion.setAmountDiscount(detalleLiquidacionRequestDTO.getAmountDiscount());
        }
        if (detalleLiquidacionRequestDTO.getPaymentPaxUSD() != null) {
            detalleLiquidacion.setPaymentPaxUSD(detalleLiquidacionRequestDTO.getPaymentPaxUSD());
        }
        if (detalleLiquidacionRequestDTO.getPaymentPaxPEN() != null) {
            detalleLiquidacion.setPaymentPaxPEN(detalleLiquidacionRequestDTO.getPaymentPaxPEN());
        }
    }
}