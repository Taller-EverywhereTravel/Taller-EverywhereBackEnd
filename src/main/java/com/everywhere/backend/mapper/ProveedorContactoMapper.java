package com.everywhere.backend.mapper;

import com.everywhere.backend.model.dto.SupplierContactRequestDTO;
import com.everywhere.backend.model.dto.SupplierContactResponseDTO;
import com.everywhere.backend.model.entity.SupplierContact;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProveedorContactoMapper {

    private final ModelMapper modelMapper;

    public SupplierContactResponseDTO toResponseDTO(SupplierContact entity) {
        SupplierContactResponseDTO dto = modelMapper.map(entity, SupplierContactResponseDTO.class);
        if (entity.getSupplier() != null) {
            dto.setSupplierId(entity.getSupplier().getId());
            dto.setSupplierName(entity.getSupplier().getName());
        }
        if (entity.getGroupContact() != null) {
            dto.setGroupContactId(entity.getGroupContact().getId());
            dto.setGroupContactName(entity.getGroupContact().getName());
        }
        return dto;
    }

    public SupplierContact toEntity(SupplierContactRequestDTO dto) {
        return modelMapper.map(dto, SupplierContact.class);
    }

    public void updateEntityFromDTO(SupplierContactRequestDTO dto, SupplierContact entity) {
        modelMapper.map(dto, entity);
    }
}
