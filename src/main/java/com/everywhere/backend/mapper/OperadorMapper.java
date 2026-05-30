package com.everywhere.backend.mapper;

import com.everywhere.backend.model.dto.OperatorRequestDTO;
import com.everywhere.backend.model.dto.OperatorResponseDTO;
import com.everywhere.backend.model.entity.Operator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OperadorMapper {

    private final ModelMapper modelMapper;

    public OperatorResponseDTO toResponseDTO(Operator operador) {
        return modelMapper.map(operador, OperatorResponseDTO.class);
    }

    public Operator toEntity(OperatorRequestDTO operadorRequestDTO) {
        return modelMapper.map(operadorRequestDTO, Operator.class);
    }

    public void updateEntityFromDTO(OperatorRequestDTO operadorRequestDTO, Operator operador) {
        modelMapper.map(operadorRequestDTO, operador);
    }
}