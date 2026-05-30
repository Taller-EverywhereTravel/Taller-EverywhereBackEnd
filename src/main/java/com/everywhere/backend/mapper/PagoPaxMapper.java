package com.everywhere.backend.mapper;

import com.everywhere.backend.model.dto.PaymentPaxRequestDTO;
import com.everywhere.backend.model.dto.PaymentPaxResponseDTO;
import com.everywhere.backend.model.entity.PaymentPax;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PagoPaxMapper {

    private final ModelMapper modelMapper;
    private final LiquidacionMapper liquidacionMapper;
    private final FormaPagoMapper formaPagoMapper;

    /**
     * Convierte una entidad PagoPax a PagoPaxResponseDTO
     */
    public PaymentPaxResponseDTO toResponseDTO(PaymentPax pagoPax) {
        if (pagoPax == null) {
            return null;
        }

        PaymentPaxResponseDTO dto = modelMapper.map(pagoPax, PaymentPaxResponseDTO.class);

        // Mapear relaciones si existen
        if (pagoPax.getLiquidation() != null) {
            dto.setLiquidation(liquidacionMapper.toResponseDTO(pagoPax.getLiquidation()));
        }

        if (pagoPax.getMethodPayment() != null) {
            dto.setMethodPayment(formaPagoMapper.toResponseDTO(pagoPax.getMethodPayment()));
        }

        return dto;
    }

    /**
     * Convierte un PagoPaxRequestDTO a entidad PagoPax
     */
    public PaymentPax toEntity(PaymentPaxRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        PaymentPax pagoPax = new PaymentPax();
        pagoPax.setAmount(requestDTO.getAmount());
        pagoPax.setCurrency(requestDTO.getCurrency());
        pagoPax.setDetail(requestDTO.getDetail());

        return pagoPax;
    }

    /**
     * Actualiza una entidad PagoPax existente con datos del RequestDTO
     */
    public void updateEntityFromRequestDTO(PaymentPax pagoPax, PaymentPaxRequestDTO requestDTO) {
        if (pagoPax == null || requestDTO == null) {
            return;
        }

        if (requestDTO.getAmount() != null) {
            pagoPax.setAmount(requestDTO.getAmount());
        }
        if (requestDTO.getCurrency() != null) {
            pagoPax.setCurrency(requestDTO.getCurrency());
        }
        if (requestDTO.getDetail() != null) {
            pagoPax.setDetail(requestDTO.getDetail());
        }
    }
}
