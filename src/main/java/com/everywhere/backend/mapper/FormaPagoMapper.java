package com.everywhere.backend.mapper;

import com.everywhere.backend.model.dto.MethodPaymentRequestDTO;
import com.everywhere.backend.model.dto.MethodPaymentResponseDTO;
import com.everywhere.backend.model.entity.MethodPayment;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FormaPagoMapper {

    @Autowired
    private ModelMapper modelMapper;

    public MethodPayment toEntity(MethodPaymentRequestDTO formaPagoRequestDTO) {
        return modelMapper.map(formaPagoRequestDTO, MethodPayment.class);
    }

    public MethodPaymentResponseDTO toResponseDTO(MethodPayment formaPago) {
        return modelMapper.map(formaPago, MethodPaymentResponseDTO.class);
    }
}