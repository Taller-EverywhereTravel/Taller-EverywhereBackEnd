package com.everywhere.backend.mapper;

import com.everywhere.backend.model.dto.SupplierGroupContactRequestDTO;
import com.everywhere.backend.model.dto.SupplierGroupContactResponseDTO;
import com.everywhere.backend.model.entity.SupplierGroupContact;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProveedorGrupoContactoMapper {

    private final ModelMapper modelMapper;

    public SupplierGroupContactResponseDTO toResponseDTO(SupplierGroupContact entity) {
        return modelMapper.map(entity, SupplierGroupContactResponseDTO.class);
    }

    public SupplierGroupContact toEntity(SupplierGroupContactRequestDTO dto) {
        return modelMapper.map(dto, SupplierGroupContact.class);
    }

    public void updateEntityFromDTO(SupplierGroupContactRequestDTO dto, SupplierGroupContact entity) {
        modelMapper.map(dto, entity);
    }
}
