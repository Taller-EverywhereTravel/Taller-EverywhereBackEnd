package com.everywhere.backend.mapper;

import com.everywhere.backend.model.dto.ProductRequestDTO;
import com.everywhere.backend.model.dto.ProductResponseDTO;
import com.everywhere.backend.model.entity.Product;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Component
@RequiredArgsConstructor
public class ProductoMapper {

    private final ModelMapper modelMapper;

    public ProductResponseDTO toResponseDTO(Product producto) {
        return modelMapper.map(producto, ProductResponseDTO.class);
    }

    public Product toEntity(ProductRequestDTO productoRequestDTO) {
        Product producto = modelMapper.map(productoRequestDTO, Product.class);
        producto.setCreated(LocalDateTime.now());
        return producto;
    }

    public void updateEntityFromDTO(ProductRequestDTO productoRequestDTO, Product producto) {
        modelMapper.map(productoRequestDTO, producto);
    }
}
