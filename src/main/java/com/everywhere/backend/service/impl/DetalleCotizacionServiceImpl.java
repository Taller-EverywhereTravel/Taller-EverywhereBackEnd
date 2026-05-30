package com.everywhere.backend.service.impl;

import com.everywhere.backend.exceptions.ResourceNotFoundException;
import com.everywhere.backend.mapper.DetalleCotizacionMapper;
import com.everywhere.backend.model.dto.DetailQuotationRequestDto;
import com.everywhere.backend.model.dto.DetailQuotationResponseDto;
import com.everywhere.backend.model.entity.*;
import com.everywhere.backend.repository.*;
import com.everywhere.backend.service.DetalleCotizacionService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;  

@Service
@RequiredArgsConstructor
public class DetalleCotizacionServiceImpl implements DetalleCotizacionService {

    private final DetalleCotizacionRepository detalleCotizacionRepository;
    private final CotizacionRepository cotizacionRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;
    private final OperadorRepository operadorRepository;
    private final DetalleCotizacionMapper detalleCotizacionMapper;

    @Override
    public List<DetailQuotationResponseDto> findAll() {
        return mapToResponseList(detalleCotizacionRepository.findAll());
    }

    @Override
    public DetailQuotationResponseDto findById(Integer id) {
        return detalleCotizacionRepository.findById(id).map(detalleCotizacionMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de cotización no encontrado con ID: " + id));
    }

    @Override
    public List<DetailQuotationResponseDto> findByCotizacionId(Integer cotizacionId) {
        if (!cotizacionRepository.existsById(cotizacionId))
            throw new ResourceNotFoundException("Cotización no encontrada con ID: " + cotizacionId);
        
        return mapToResponseList(detalleCotizacionRepository.findByCotizacionId(cotizacionId));
    }

    @Override
    public DetailQuotationResponseDto create(DetailQuotationRequestDto detalleCotizacionRequestDto, Integer cotizacionId) {
        if (cotizacionId == null) throw new IllegalArgumentException("El ID de la cotización es obligatorio");
        
        DetailQuotation detalleCotizacion = detalleCotizacionMapper.toEntity(detalleCotizacionRequestDto);

        if (!cotizacionRepository.existsById(cotizacionId))
            throw new ResourceNotFoundException("Cotización no encontrada con ID: " + cotizacionId);
        detalleCotizacion.setQuotation(cotizacionRepository.findById(cotizacionId).get());

        if (detalleCotizacionRequestDto.getCategoryId() != null) {
            if (!categoriaRepository.existsById(detalleCotizacionRequestDto.getCategoryId()))
                throw new ResourceNotFoundException("Categoría no encontrada con ID: " + detalleCotizacionRequestDto.getCategoryId());
            detalleCotizacion.setCategory(categoriaRepository.findById(detalleCotizacionRequestDto.getCategoryId()).get());
        }

        if (detalleCotizacionRequestDto.getProductId() != null) {
            if (!productoRepository.existsById(detalleCotizacionRequestDto.getProductId()))
                throw new ResourceNotFoundException("Producto no encontrado con ID: " + detalleCotizacionRequestDto.getProductId());
            detalleCotizacion.setProduct(productoRepository.findById(detalleCotizacionRequestDto.getProductId()).get());
        }

        if (detalleCotizacionRequestDto.getSupplierId() != null) {
            if (!proveedorRepository.existsById(detalleCotizacionRequestDto.getSupplierId()))
                throw new ResourceNotFoundException("Proveedor no encontrado con ID: " + detalleCotizacionRequestDto.getSupplierId());
            detalleCotizacion.setSupplier(proveedorRepository.findById(detalleCotizacionRequestDto.getSupplierId()).get());
        }

        if (detalleCotizacionRequestDto.getOperatorId() != null) {
            if (!operadorRepository.existsById(detalleCotizacionRequestDto.getOperatorId()))
                throw new ResourceNotFoundException("Operador no encontrado con ID: " + detalleCotizacionRequestDto.getOperatorId());
            detalleCotizacion.setOperator(operadorRepository.findById(detalleCotizacionRequestDto.getOperatorId()).get());
        }

        return detalleCotizacionMapper.toResponse(detalleCotizacionRepository.save(detalleCotizacion));
    }

    @Override
    public DetailQuotationResponseDto patch(Integer id, DetailQuotationRequestDto detalleCotizacionRequestDto) {
        if (!detalleCotizacionRepository.existsById(id))
            throw new ResourceNotFoundException("Detalle de cotización no encontrado con ID: " + id);

        DetailQuotation detalleCotizacion = detalleCotizacionRepository.findById(id).get();
        detalleCotizacionMapper.updateEntityFromRequest(detalleCotizacion, detalleCotizacionRequestDto);

        if (detalleCotizacionRequestDto.getCategoryId() != null) {
            if (!categoriaRepository.existsById(detalleCotizacionRequestDto.getCategoryId()))
                throw new ResourceNotFoundException("Categoría no encontrada con ID: " + detalleCotizacionRequestDto.getCategoryId());
            detalleCotizacion.setCategory(categoriaRepository.findById(detalleCotizacionRequestDto.getCategoryId()).get());
        }

        if (detalleCotizacionRequestDto.getProductId() != null) {
            if (!productoRepository.existsById(detalleCotizacionRequestDto.getProductId()))
                throw new ResourceNotFoundException("Producto no encontrado con ID: " + detalleCotizacionRequestDto.getProductId());
            detalleCotizacion.setProduct(productoRepository.findById(detalleCotizacionRequestDto.getProductId()).get());
        }

        if (detalleCotizacionRequestDto.getSupplierId() != null) {
            if (!proveedorRepository.existsById(detalleCotizacionRequestDto.getSupplierId()))
                throw new ResourceNotFoundException("Proveedor no encontrado con ID: " + detalleCotizacionRequestDto.getSupplierId());
            detalleCotizacion.setSupplier(proveedorRepository.findById(detalleCotizacionRequestDto.getSupplierId()).get());
        }

        if (detalleCotizacionRequestDto.getOperatorId() != null) {
            if (!operadorRepository.existsById(detalleCotizacionRequestDto.getOperatorId()))
                throw new ResourceNotFoundException("Operador no encontrado con ID: " + detalleCotizacionRequestDto.getOperatorId());
            detalleCotizacion.setOperator(operadorRepository.findById(detalleCotizacionRequestDto.getOperatorId()).get());
        }

        return detalleCotizacionMapper.toResponse(detalleCotizacionRepository.save(detalleCotizacion));
    }

    @Override
    public void delete(Integer id) {
        if (!detalleCotizacionRepository.existsById(id))
            throw new ResourceNotFoundException("Detalle de cotización no encontrado con ID: " + id);
        detalleCotizacionRepository.deleteById(id);
    }

    private List<DetailQuotationResponseDto> mapToResponseList(List<DetailQuotation> detalles) {
        return detalles.stream().map(detalleCotizacionMapper::toResponse).toList();
    }
}