package com.everywhere.backend.mapper;

import com.everywhere.backend.model.dto.BranchRequestDTO;
import com.everywhere.backend.model.dto.BranchResponseDTO;
import com.everywhere.backend.model.entity.Branch;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SucursalMapper {

    @Autowired
    private ModelMapper modelMapper;
    public Branch toEntity(BranchRequestDTO sucursalRequestDTO) {
        return modelMapper.map(sucursalRequestDTO, Branch.class);
    }
    public BranchResponseDTO toResponseDTO(Branch sucursal) {
        return modelMapper.map(sucursal, BranchResponseDTO.class);
    }

    public void updateEntityFromDTO(BranchRequestDTO sucursalRequestDTO, Branch sucursal) {
        modelMapper.map(sucursalRequestDTO, sucursal);
    }


}
