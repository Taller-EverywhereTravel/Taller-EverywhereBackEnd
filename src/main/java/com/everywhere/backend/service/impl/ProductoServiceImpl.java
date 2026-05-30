package com.everywhere.backend.service.impl;

import com.everywhere.backend.exceptions.ConflictException;
import com.everywhere.backend.exceptions.ResourceNotFoundException;
import com.everywhere.backend.mapper.ProductoMapper;
import com.everywhere.backend.model.dto.ProductRequestDTO;
import com.everywhere.backend.model.dto.ProductResponseDTO; 
import com.everywhere.backend.model.entity.Product;
import com.everywhere.backend.repository.DetalleCotizacionRepository;
import com.everywhere.backend.repository.DetalleLiquidacionRepository;
import com.everywhere.backend.repository.ProductoRepository;
import com.everywhere.backend.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors; 

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;
    private final DetalleCotizacionRepository detalleCotizacionRepository;
    private final DetalleLiquidacionRepository detalleLiquidacionRepository;

    @Override
    public ProductResponseDTO create(ProductRequestDTO productoRequestDTO) {
        Product producto = productoMapper.toEntity(productoRequestDTO);
        return productoMapper.toResponseDTO(productoRepository.save(producto));
    }

    @Override
    public ProductResponseDTO update(Integer id, ProductRequestDTO productoRequestDTO) {
        if (!productoRepository.existsById(id))
            throw new ResourceNotFoundException("Producto no encontrado con ID: " + id);

        Product producto = productoRepository.findById(id).get();
        
        if (productoRequestDTO.getType() != null && 
            productoRepository.existsProductByType(productoRequestDTO.getType()) &&
            !productoRequestDTO.getType().equals(producto.getType())) {
            throw new DataIntegrityViolationException("Ya existe un producto con el tipo: " + productoRequestDTO.getType());
        }

        productoMapper.updateEntityFromDTO(productoRequestDTO, producto);
        return productoMapper.toResponseDTO(productoRepository.save(producto));
    }

    @Override
    public ProductResponseDTO getById(Integer id) {
        Product producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        return productoMapper.toResponseDTO(producto);
    }

    @Override
    public List<ProductResponseDTO> getAll() {
        return mapToResponseList(productoRepository.findAll());
    }

    @Override
    public void delete(Integer id) {
        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado con ID: " + id);
        }

        long cotizacionesCount = detalleCotizacionRepository.countByProductId(id);
        if (cotizacionesCount > 0) {
            throw new ConflictException(
                    "No se puede eliminar este producto porque tiene " + cotizacionesCount + " cotización(es) asociada(s).",
                    "/api/v1/producto/" + id
            );
        }

        long liquidacionesCount = detalleLiquidacionRepository.countByProductId(id);
        if (liquidacionesCount > 0) {
            throw new ConflictException(
                    "No se puede eliminar este producto porque tiene " + liquidacionesCount + " liquidación(es) asociada(s).",
                    "/api/v1/producto/" + id
            );
        }

        productoRepository.deleteById(id);
    }

    private List<ProductResponseDTO> mapToResponseList(List<Product> productos) {
        return productos.stream()
                .map(productoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

}
