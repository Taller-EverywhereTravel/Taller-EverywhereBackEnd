package com.everywhere.backend.service.impl;

import com.everywhere.backend.exceptions.ResourceNotFoundException;
import com.everywhere.backend.mapper.OperadorMapper;
import com.everywhere.backend.model.dto.OperatorRequestDTO;
import com.everywhere.backend.model.dto.OperatorResponseDTO;
import com.everywhere.backend.model.entity.Operator;
import com.everywhere.backend.repository.OperadorRepository;
import com.everywhere.backend.service.OperadorService;
import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OperadorServiceImpl implements OperadorService {

    private final OperadorRepository operadorRepository;
    private final OperadorMapper operadorMapper;

    @Override
    public List<OperatorResponseDTO> findAll() {
        return mapToResponseList(operadorRepository.findAll());
    }

    @Override
    public OperatorResponseDTO findById(int id) {
        Operator operador = operadorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Operador no encontrado con ID: " + id));
        return operadorMapper.toResponseDTO(operador);
    }

    @Override
    public OperatorResponseDTO findByNombre(String nombre) {
        Operator operador = operadorRepository.findByNombre(nombre)
                .orElseThrow(() -> new ResourceNotFoundException("Operador no encontrado con nombre: " + nombre));

        return operadorMapper.toResponseDTO(operador);
    }

    @Override
    public OperatorResponseDTO save(OperatorRequestDTO operadorRequestDTO) {
        if (operadorRepository.existsByNombreIgnoreCase(operadorRequestDTO.getName()))
            throw new DataIntegrityViolationException("Ya existe un operador con el nombre: " + operadorRequestDTO.getName());
        Operator operador = operadorMapper.toEntity(operadorRequestDTO);
        return operadorMapper.toResponseDTO(operadorRepository.save(operador));
    }

    @Override
    public OperatorResponseDTO update(int id, OperatorRequestDTO operadorRequestDTO) {
        if (!operadorRepository.existsById(id))
            throw new ResourceNotFoundException("Operador con id " + id + " no encontrado");

        Operator operador = operadorRepository.findById(id).get();

        if (operadorRequestDTO.getName() != null && 
            !operadorRequestDTO.getName().equalsIgnoreCase(operador.getName()) &&
            operadorRepository.existsByNombreIgnoreCase(operadorRequestDTO.getName())) {
            throw new DataIntegrityViolationException("Ya existe otro operador con el nombre: " + operadorRequestDTO.getName());
        }
        
        operadorMapper.updateEntityFromDTO(operadorRequestDTO, operador);
        return operadorMapper.toResponseDTO(operadorRepository.save(operador));
    }

    @Override
    public void deleteById(int id) {
        if (!operadorRepository.existsById(id))
            throw new ResourceNotFoundException("Operador no encontrado con ID: " + id);
        operadorRepository.deleteById(id);
    }

    private List<OperatorResponseDTO> mapToResponseList(List<Operator> operadores) {
        return operadores.stream().map(operadorMapper::toResponseDTO).toList();
    }
}
