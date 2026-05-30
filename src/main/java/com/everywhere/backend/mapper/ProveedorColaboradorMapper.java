package com.everywhere.backend.mapper;

import com.everywhere.backend.model.dto.SupplierCollaboratorRequestDTO;
import com.everywhere.backend.model.dto.SupplierCollaboratorResponseDTO;
import com.everywhere.backend.model.entity.SupplierCollaborator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProveedorColaboradorMapper {

    private final ModelMapper modelMapper;

    public SupplierCollaboratorResponseDTO toResponseDTO(SupplierCollaborator entity) {
        SupplierCollaboratorResponseDTO dto = modelMapper.map(entity, SupplierCollaboratorResponseDTO.class);
        if (entity.getSupplier() != null) {
            dto.setSupplierId(entity.getSupplier().getId());
            dto.setSupplierName(entity.getSupplier().getName());
        }
        return dto;
    }

    public SupplierCollaborator toEntity(SupplierCollaboratorRequestDTO dto) {
        return modelMapper.map(dto, SupplierCollaborator.class);
    }

    public void updateEntityFromDTO(SupplierCollaboratorRequestDTO dto, SupplierCollaborator entity) {
        modelMapper.map(dto, entity);
    }
}
