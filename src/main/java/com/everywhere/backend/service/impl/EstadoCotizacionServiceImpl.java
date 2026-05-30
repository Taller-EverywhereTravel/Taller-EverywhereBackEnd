package com.everywhere.backend.service.impl;

import com.everywhere.backend.exceptions.ConflictException;
import com.everywhere.backend.exceptions.ResourceNotFoundException;
import com.everywhere.backend.mapper.EstadoCotizacionMapper;
import com.everywhere.backend.model.dto.StatusQuotationRequestDTO;
import com.everywhere.backend.model.dto.StatusQuotationResponseDTO;
import com.everywhere.backend.model.entity.StatusQuotation;
import com.everywhere.backend.repository.CotizacionRepository;
import com.everywhere.backend.repository.EstadoCotizacionRepository;
import com.everywhere.backend.service.EstadoCotizacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstadoCotizacionServiceImpl implements EstadoCotizacionService {

    private final EstadoCotizacionRepository estadoCotizacionRepository;
    private final EstadoCotizacionMapper estadoCotizacionMapper;
    private final CotizacionRepository cotizacionRepository;

    @Override
    public StatusQuotationResponseDTO create(StatusQuotationRequestDTO estadoCotizacionRequestDTO) {
        StatusQuotation estadoCotizacion = estadoCotizacionMapper.toEntity(estadoCotizacionRequestDTO); 
        return estadoCotizacionMapper.toResponseDTO(estadoCotizacionRepository.save(estadoCotizacion));
    }

    @Override
    public StatusQuotationResponseDTO update(Integer id, StatusQuotationRequestDTO estadoCotizacionRequestDTO) {
        if (!estadoCotizacionRepository.existsById(id))
            throw new ResourceNotFoundException("Estado de Cotización no encontrado con ID: " + id);

        StatusQuotation existing = estadoCotizacionRepository.findById(id).get();
        estadoCotizacionMapper.updateEntityFromDTO(estadoCotizacionRequestDTO, existing); 
        return estadoCotizacionMapper.toResponseDTO(estadoCotizacionRepository.save(existing));
    }

    @Override
    public StatusQuotationResponseDTO getById(Integer id) {
        return estadoCotizacionRepository.findById(id).map(estadoCotizacionMapper::toResponseDTO)
            .orElseThrow(() -> new ResourceNotFoundException("Estado de Cotización no encontrado con ID: " + id));
    }

    @Override
    public List<StatusQuotationResponseDTO> getAll() {
        return mapToResponseList(estadoCotizacionRepository.findAll());
    }

    @Override
    public void delete(Integer ida) {
        if (!estadoCotizacionRepository.existsById(ida))
            throw new ResourceNotFoundException("Estado de Cotización no encontrado con ID: " + ida);

        // Validar que no existan cotizaciones vinculadas a este estado
        Long cotizacionesCount = cotizacionRepository.countByStatusQuotationId(ida);
        if (cotizacionesCount > 0) {
            throw new ConflictException(
                    "No se puede eliminar el Estado de Cotización porque hay " + cotizacionesCount +
                            " cotización(es) vinculada(s).",
                    "/api/v1/estados-cotizacion/" + ida
            );
        }

        estadoCotizacionRepository.deleteById(ida);
    }

    private List<StatusQuotationResponseDTO> mapToResponseList(List<StatusQuotation> estadosCotizacion) {
        return estadosCotizacion.stream().map(estadoCotizacionMapper::toResponseDTO).toList();
    }
}