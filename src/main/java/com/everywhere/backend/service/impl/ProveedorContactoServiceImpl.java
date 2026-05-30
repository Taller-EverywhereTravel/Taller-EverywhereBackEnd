package com.everywhere.backend.service.impl;

import com.everywhere.backend.exceptions.BadRequestException;
import com.everywhere.backend.exceptions.ResourceNotFoundException;
import com.everywhere.backend.mapper.ProveedorContactoMapper;
import com.everywhere.backend.model.dto.SupplierContactRequestDTO;
import com.everywhere.backend.model.dto.SupplierContactResponseDTO;
import com.everywhere.backend.model.entity.Supplier;
import com.everywhere.backend.model.entity.SupplierContact;
import com.everywhere.backend.model.entity.SupplierGroupContact;
import com.everywhere.backend.repository.ProveedorContactoRepository;
import com.everywhere.backend.repository.ProveedorRepository;
import com.everywhere.backend.repository.ProveedorGrupoContactoRepository;
import com.everywhere.backend.service.ProveedorContactoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProveedorContactoServiceImpl implements ProveedorContactoService {

    private final ProveedorContactoRepository repository;
    private final ProveedorRepository proveedorRepository;
    private final ProveedorGrupoContactoRepository grupoContactoRepository;
    private final ProveedorContactoMapper mapper;

    @Override
    public List<SupplierContactResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public SupplierContactResponseDTO findById(Integer id) {
        SupplierContact entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contacto de proveedor no encontrado con ID: " + id));
        return mapper.toResponseDTO(entity);
    }

    @Override
    public List<SupplierContactResponseDTO> findByProveedorId(Integer proveedorId) {
        return repository.findByProveedorId(proveedorId).stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<SupplierContactResponseDTO> findByGrupoContactoId(Integer grupoContactoId) {
        return repository.findByGrupoContactoId(grupoContactoId).stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public SupplierContactResponseDTO save(SupplierContactRequestDTO dto) {
        SupplierContact entity = mapper.toEntity(dto);

        // Validar y asignar proveedor si está presente
        if (dto.getSupplierId() != null) {
            Supplier proveedor = proveedorRepository.findById(dto.getSupplierId())
                    .orElseThrow(
                            () -> new BadRequestException("Proveedor no encontrado con ID: " + dto.getSupplierId()));
            entity.setSupplier(proveedor);
        }

        // Validar y asignar grupo de contacto si está presente
        if (dto.getGroupContactId() != null) {
            SupplierGroupContact grupo = grupoContactoRepository.findById(dto.getGroupContactId())
                    .orElseThrow(() -> new BadRequestException(
                            "Grupo de contacto no encontrado con ID: " + dto.getGroupContactId()));
            entity.setGroupContact(grupo);
        }

        return mapper.toResponseDTO(repository.save(entity));
    }

    @Override
    public SupplierContactResponseDTO update(Integer id, SupplierContactRequestDTO dto) {
        SupplierContact existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contacto de proveedor no encontrado con ID: " + id));

        mapper.updateEntityFromDTO(dto, existing);

        // Actualizar proveedor si se proporciona
        if (dto.getSupplierId() != null) {
            Supplier proveedor = proveedorRepository.findById(dto.getSupplierId())
                    .orElseThrow(
                            () -> new BadRequestException("Proveedor no encontrado con ID: " + dto.getSupplierId()));
            existing.setSupplier(proveedor);
        }

        // Actualizar grupo si se proporciona
        if (dto.getGroupContactId() != null) {
            SupplierGroupContact grupo = grupoContactoRepository.findById(dto.getGroupContactId())
                    .orElseThrow(() -> new BadRequestException(
                            "Grupo de contacto no encontrado con ID: " + dto.getGroupContactId()));
            existing.setGroupContact(grupo);
        }

        return mapper.toResponseDTO(repository.save(existing));
    }

    @Override
    public void deleteById(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Contacto de proveedor no encontrado con ID: " + id);
        }
        repository.deleteById(id);
    }
}
