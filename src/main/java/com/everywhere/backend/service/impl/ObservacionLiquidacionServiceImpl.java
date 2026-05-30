package com.everywhere.backend.service.impl;

import com.everywhere.backend.exceptions.ResourceNotFoundException;
import com.everywhere.backend.mapper.ObservacionLiquidacionMapper;
import com.everywhere.backend.model.dto.ObservationLiquidationRequestDTO;
import com.everywhere.backend.model.dto.ObservationLiquidationResponseDTO;
import com.everywhere.backend.model.entity.Liquidation;
import com.everywhere.backend.model.entity.ObservationLiquidation;
import com.everywhere.backend.repository.LiquidacionRepository;
import com.everywhere.backend.repository.ObservacionLiquidacionRepository;
import com.everywhere.backend.service.ObservacionLiquidacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ObservacionLiquidacionServiceImpl implements ObservacionLiquidacionService {

    private final ObservacionLiquidacionRepository observacionLiquidacionRepository;
    private final ObservacionLiquidacionMapper observacionLiquidacionMapper;
    private final LiquidacionRepository liquidacionRepository;

    @Override
    public List<ObservationLiquidationResponseDTO> findAll() {
        return mapToResponseList(observacionLiquidacionRepository.findAll());
    }

    @Override
    public ObservationLiquidationResponseDTO findById(Long id) {
        ObservationLiquidation observacionLiquidacion = observacionLiquidacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Observación de liquidación no encontrada con ID: " + id));

        return observacionLiquidacionMapper.toResponseDTO(observacionLiquidacion);
    }

    @Override
    public ObservationLiquidationResponseDTO save(ObservationLiquidationRequestDTO observacionLiquidacionRequestDTO) { 
        if (observacionLiquidacionRequestDTO.getLiquidationId() != null && 
            !liquidacionRepository.existsById(observacionLiquidacionRequestDTO.getLiquidationId())) {
            throw new ResourceNotFoundException(
                    "Liquidación no encontrada con id " + observacionLiquidacionRequestDTO.getLiquidationId());
        }

        ObservationLiquidation observacionLiquidacion = observacionLiquidacionMapper.toEntity(observacionLiquidacionRequestDTO);

        if (observacionLiquidacionRequestDTO.getLiquidationId() != null) {
            Liquidation liquidacion = liquidacionRepository.findById(observacionLiquidacionRequestDTO.getLiquidationId()).get();
            observacionLiquidacion.setLiquidation(liquidacion);
        }
        return observacionLiquidacionMapper.toResponseDTO(observacionLiquidacionRepository.save(observacionLiquidacion));
    }

    @Override
    public ObservationLiquidationResponseDTO update(Long id, ObservationLiquidationRequestDTO observacionLiquidacionRequestDTO) { 
        if (!observacionLiquidacionRepository.existsById(id))
            throw new ResourceNotFoundException("Observación de liquidación no encontrada con ID: " + id);
 
        if (observacionLiquidacionRequestDTO.getLiquidationId() != null && 
            !liquidacionRepository.existsById(observacionLiquidacionRequestDTO.getLiquidationId())) {
            throw new ResourceNotFoundException(
                    "Liquidación no encontrada con id " + observacionLiquidacionRequestDTO.getLiquidationId());
        }
 
        ObservationLiquidation observacionLiquidacion = observacionLiquidacionRepository.findById(id).get();
        observacionLiquidacionMapper.updateEntityFromDTO(observacionLiquidacionRequestDTO, observacionLiquidacion);

        if (observacionLiquidacionRequestDTO.getLiquidationId() != null) {
            Liquidation liquidacion = liquidacionRepository.findById(observacionLiquidacionRequestDTO.getLiquidationId()).get();
            observacionLiquidacion.setLiquidation(liquidacion);
        }

        return observacionLiquidacionMapper.toResponseDTO(observacionLiquidacionRepository.save(observacionLiquidacion));
    }

    @Override
    public void deleteById(Long id) {
        if (!observacionLiquidacionRepository.existsById(id))
            throw new ResourceNotFoundException("No existe una observación de liquidación con ID: " + id);
        observacionLiquidacionRepository.deleteById(id);
    }

    @Override
    public List<ObservationLiquidationResponseDTO> findByLiquidacionId(Integer liquidacionId) { 
        return mapToResponseList(observacionLiquidacionRepository.findByLiquidacionId(liquidacionId));
    }

    private List<ObservationLiquidationResponseDTO> mapToResponseList(List<ObservationLiquidation> observaciones) {
        return observaciones.stream().map(observacionLiquidacionMapper::toResponseDTO).toList();
    }
}