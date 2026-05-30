package com.everywhere.backend.mapper;

import com.everywhere.backend.model.dto.SupplierRequestDTO;
import com.everywhere.backend.model.dto.SupplierResponseDTO;
import com.everywhere.backend.model.entity.Supplier;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProveedorMapper {

    @Autowired
    private ModelMapper modelMapper;

    public Supplier toEntity(SupplierRequestDTO proveedorRequestDTO) {
        return modelMapper.map(proveedorRequestDTO, Supplier.class);
    }

    public SupplierResponseDTO toResponseDTO(Supplier proveedor) {
        return modelMapper.map(proveedor, SupplierResponseDTO.class);
    }

    public void updateEntityFromDTO(SupplierRequestDTO proveedorRequestDTO, Supplier proveedor) {
        modelMapper.map(proveedorRequestDTO, proveedor);
    }
}
