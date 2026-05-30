package com.everywhere.backend.service.impl;

import com.everywhere.backend.exceptions.BadRequestException;
import com.everywhere.backend.exceptions.ResourceNotFoundException;
import com.everywhere.backend.mapper.ProveedorColaboradorMapper;
import com.everywhere.backend.model.dto.SupplierCollaboratorRequestDTO;
import com.everywhere.backend.model.dto.SupplierCollaboratorResponseDTO;
import com.everywhere.backend.model.entity.Supplier;
import com.everywhere.backend.model.entity.SupplierCollaborator;
import com.everywhere.backend.repository.ProveedorColaboradorRepository;
import com.everywhere.backend.repository.ProveedorRepository;
import com.everywhere.backend.service.ProveedorColaboradorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProveedorColaboradorServiceImpl implements ProveedorColaboradorService {

    private final ProveedorColaboradorRepository repository;
    private final ProveedorRepository proveedorRepository;
    private final ProveedorColaboradorMapper mapper;

    @Override
    public List<SupplierCollaboratorResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public SupplierCollaboratorResponseDTO findById(Integer id) {
        SupplierCollaborator entity = repository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Colaborador de proveedor no encontrado con ID: " + id));
        return mapper.toResponseDTO(entity);
    }

    @Override
    public List<SupplierCollaboratorResponseDTO> findByProveedorId(Integer proveedorId) {
        return repository.findByProveedorId(proveedorId).stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public SupplierCollaboratorResponseDTO save(SupplierCollaboratorRequestDTO dto) {
        SupplierCollaborator entity = mapper.toEntity(dto);

        // Validar y asignar proveedor si está presente
        if (dto.getSupplierId() != null) {
            Supplier proveedor = proveedorRepository.findById(dto.getSupplierId())
                    .orElseThrow(
                            () -> new BadRequestException("Proveedor no encontrado con ID: " + dto.getSupplierId()));
            entity.setSupplier(proveedor);
        }

        return mapper.toResponseDTO(repository.save(entity));
    }

    @Override
    public SupplierCollaboratorResponseDTO update(Integer id, SupplierCollaboratorRequestDTO dto) {
        SupplierCollaborator existing = repository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Colaborador de proveedor no encontrado con ID: " + id));

        mapper.updateEntityFromDTO(dto, existing);

        // Actualizar proveedor si se proporciona
        if (dto.getSupplierId() != null) {
            Supplier proveedor = proveedorRepository.findById(dto.getSupplierId())
                    .orElseThrow(
                            () -> new BadRequestException("Proveedor no encontrado con ID: " + dto.getSupplierId()));
            existing.setSupplier(proveedor);
        }

        return mapper.toResponseDTO(repository.save(existing));
    }

    @Override
    public void deleteById(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Colaborador de proveedor no encontrado con ID: " + id);
        }
        repository.deleteById(id);
    }
}
