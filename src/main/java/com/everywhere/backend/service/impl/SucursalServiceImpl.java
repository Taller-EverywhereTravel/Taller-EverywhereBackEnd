package com.everywhere.backend.service.impl;

import com.everywhere.backend.exceptions.BadRequestException;
import com.everywhere.backend.exceptions.ResourceNotFoundException;
import com.everywhere.backend.mapper.SucursalMapper;
import com.everywhere.backend.model.dto.BranchRequestDTO;
import com.everywhere.backend.model.dto.BranchResponseDTO;
import com.everywhere.backend.model.entity.Branch;
import com.everywhere.backend.repository.SucursalRepository;
import com.everywhere.backend.service.SucursalService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SucursalServiceImpl implements SucursalService {

    private final SucursalRepository sucursalRepository;
    private final SucursalMapper sucursalMapper;

    @Override
    public List<BranchResponseDTO> findAll() {
        return mapToResponseList(sucursalRepository.findAll());
    }

    @Override
    public BranchResponseDTO findById(Integer id) {
        Branch sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada con ID: " + id));
        return sucursalMapper.toResponseDTO(sucursal);
    }

    @Override
    public List<BranchResponseDTO> findByDescripcion(String descripcion) {
        return mapToResponseList(sucursalRepository.findByDescripcionContainingIgnoreCase(descripcion));
    }

    @Override
    public BranchResponseDTO findByDescripcionExacta(String descripcion) {
        Branch sucursal = sucursalRepository.findByDescripcionIgnoreCase(descripcion)
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada con descripción: " + descripcion));
        return sucursalMapper.toResponseDTO(sucursal);
    }

    @Override
    public List<BranchResponseDTO> findByEstado(Boolean estado) {
        return mapToResponseList(sucursalRepository.findByEstado(estado));
    }

    @Override
    public List<BranchResponseDTO> findByEstadoAndDescripcion(Boolean estado, String descripcion) {
        return mapToResponseList(sucursalRepository.findByEstadoAndDescripcionContainingIgnoreCase(estado, descripcion));
    }

    @Override
    public List<BranchResponseDTO> findByDireccion(String direccion) {
        return mapToResponseList(sucursalRepository.findByDireccionContainingIgnoreCase(direccion));
    }

    @Override
    public BranchResponseDTO findByEmail(String email) {
        Branch sucursal = sucursalRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada con email: " + email));
        return sucursalMapper.toResponseDTO(sucursal);
    }

    @Override
    public BranchResponseDTO save(BranchRequestDTO sucursalRequestDTO) {
        if (sucursalRequestDTO.getMail() != null &&
                !sucursalRequestDTO.getMail().trim().isEmpty() &&
                sucursalRepository.existsByEmail(sucursalRequestDTO.getMail())) {
            throw new DataIntegrityViolationException("Ya existe una sucursal con el email: " + sucursalRequestDTO.getMail());
        }

        Branch sucursal = sucursalMapper.toEntity(sucursalRequestDTO);
        if (sucursal.getStatus() == null) sucursal.setStatus(true);

        return sucursalMapper.toResponseDTO(sucursalRepository.save(sucursal));
    }

    @Override
    public BranchResponseDTO update(Integer id, BranchRequestDTO sucursalRequestDTO) {
        if (!sucursalRepository.existsById(id))
            throw new ResourceNotFoundException("Sucursal no encontrada con ID: " + id);

        Branch existing = sucursalRepository.findById(id).get();

        if (sucursalRequestDTO.getMail() != null &&
                !sucursalRequestDTO.getMail().trim().isEmpty() &&
                !sucursalRequestDTO.getMail().equals(existing.getMail()) &&
                sucursalRepository.existsByEmail(sucursalRequestDTO.getMail())) {
            throw new BadRequestException("Ya existe una sucursal con el email: " + sucursalRequestDTO.getMail());
        }

        sucursalMapper.updateEntityFromDTO(sucursalRequestDTO, existing);
        return sucursalMapper.toResponseDTO(sucursalRepository.save(existing));
    }

    @Override
    public void deleteById(Integer id) {
        if (!sucursalRepository.existsById(id))
            throw new ResourceNotFoundException("Sucursal no encontrada con ID: " + id);
        sucursalRepository.deleteById(id);
    }

    @Override
    public BranchResponseDTO cambiarEstado(Integer id, Boolean estado) {
        if (!sucursalRepository.existsById(id))
            throw new ResourceNotFoundException("Sucursal no encontrada con ID: " + id);

        Branch sucursal = sucursalRepository.findById(id).get();
        sucursal.setStatus(estado);
        return sucursalMapper.toResponseDTO(sucursalRepository.save(sucursal));
    }

    private List<BranchResponseDTO> mapToResponseList(List<Branch> sucursales) {
        return sucursales.stream().map(sucursalMapper::toResponseDTO).toList();
    }
}