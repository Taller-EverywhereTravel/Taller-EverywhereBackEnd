package com.everywhere.backend.service.impl;

import com.everywhere.backend.exceptions.ConflictException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.everywhere.backend.service.CategoriaPersonaService;
import com.everywhere.backend.repository.CategoriaPersonaRepository;
import com.everywhere.backend.repository.PersonaNaturalRepository;
import com.everywhere.backend.mapper.CategoriaPersonaMapper;
import com.everywhere.backend.mapper.PersonaNaturalMapper;
import com.everywhere.backend.model.dto.CategoryPersonaRequestDTO;
import com.everywhere.backend.model.dto.CategoryPersonaResponseDTO;
import com.everywhere.backend.model.dto.PersonNaturalResponseDTO;
import com.everywhere.backend.model.entity.CategoryPerson;
import com.everywhere.backend.model.entity.PersonNatural;
import com.everywhere.backend.exceptions.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoriaPersonaServiceImpl implements CategoriaPersonaService {
    
    private final CategoriaPersonaRepository categoriaPersonaRepository;
    private final CategoriaPersonaMapper categoriaPersonaMapper;
    private final PersonaNaturalRepository personaNaturalRepository;
    private final PersonaNaturalMapper personaNaturalMapper;

    @Override
    public List<CategoryPersonaResponseDTO> findAll() {
        return mapToResponseList(categoriaPersonaRepository.findAll());
    }

    @Override
    public CategoryPersonaResponseDTO findById(Integer id) {
        CategoryPerson categoria = categoriaPersonaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría de persona no encontrada con ID: " + id));
        return categoriaPersonaMapper.toResponseDTO(categoria);
    }

    @Override
    public List<CategoryPersonaResponseDTO> findByNombre(String nombre) { 
        return mapToResponseList(categoriaPersonaRepository.findByNombreContainingIgnoreCase(nombre));
    }

    @Override
    @Transactional
    public CategoryPersonaResponseDTO save(CategoryPersonaRequestDTO categoriaPersonaRequestDTO) {
        if (categoriaPersonaRepository.existsByNombreIgnoreCase(categoriaPersonaRequestDTO.getName()))
            throw new DataIntegrityViolationException("Ya existe una categoría con el nombre: " + categoriaPersonaRequestDTO.getName());
        CategoryPerson categoria = categoriaPersonaMapper.toEntity(categoriaPersonaRequestDTO); 
        return categoriaPersonaMapper.toResponseDTO(categoriaPersonaRepository.save(categoria));
    }

    @Override
    @Transactional
    public CategoryPersonaResponseDTO patch(Integer id, CategoryPersonaRequestDTO categoriaPersonaRequestDTO) {
        if (!categoriaPersonaRepository.existsById(id))
            throw new ResourceNotFoundException("Categoría de persona no encontrada con ID: " + id);
        
        if (categoriaPersonaRequestDTO.getName() != null && 
            categoriaPersonaRepository.existsByNombreIgnoreCase(categoriaPersonaRequestDTO.getName())) {
            CategoryPerson existing = categoriaPersonaRepository.findById(id).get();
            if (!categoriaPersonaRequestDTO.getName().equalsIgnoreCase(existing.getName())) 
                throw new DataIntegrityViolationException("Ya existe una categoría con el nombre: " + categoriaPersonaRequestDTO.getName());
        }
        
        CategoryPerson categoriaPersona = categoriaPersonaRepository.findById(id).get();
        categoriaPersonaMapper.updateEntityFromDTO(categoriaPersonaRequestDTO, categoriaPersona); 
        return categoriaPersonaMapper.toResponseDTO(categoriaPersonaRepository.save(categoriaPersona));
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        if (!categoriaPersonaRepository.existsById(id))
            throw new ResourceNotFoundException("Categoría de persona no encontrada con ID: " + id);

        long personasNaturalesCount = personaNaturalRepository.countByCategoriaPersonaId(id);
        if (personasNaturalesCount > 0) {
            throw new ConflictException(
                    "No se puede eliminar esta categoría porque tiene " + personasNaturalesCount + " persona(s) natural(es) asociada(s).",
                    "/api/v1/categorias-persona/" + id
            );
        }

        categoriaPersonaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public PersonNaturalResponseDTO asignarCategoria(Integer personaNaturalId, Integer categoriaId) {
        if (!personaNaturalRepository.existsById(personaNaturalId))
            throw new ResourceNotFoundException("Persona natural no encontrada con ID: " + personaNaturalId);
        
        if (!categoriaPersonaRepository.existsById(categoriaId))
            throw new ResourceNotFoundException("Categoría no encontrada con ID: " + categoriaId);
        
        PersonNatural personaNatural = personaNaturalRepository.findById(personaNaturalId).get();
        CategoryPerson categoria = categoriaPersonaRepository.findById(categoriaId).get();
        
        personaNatural.setCategoryPerson(categoria); 
        return personaNaturalMapper.toResponseDTO(personaNaturalRepository.save(personaNatural));
    }

    @Override
    @Transactional
    public PersonNaturalResponseDTO desasignarCategoria(Integer personaNaturalId) {
        if (!personaNaturalRepository.existsById(personaNaturalId))
            throw new ResourceNotFoundException("Persona natural no encontrada con ID: " + personaNaturalId);

        PersonNatural personaNatural = personaNaturalRepository.findById(personaNaturalId).get();
        personaNatural.setCategoryPerson(null);
        return personaNaturalMapper.toResponseDTO(personaNaturalRepository.save(personaNatural));
    }

    @Override
    public List<PersonNaturalResponseDTO> findPersonasPorCategoria(Integer categoriaId) {
        if (!categoriaPersonaRepository.existsById(categoriaId))
            throw new ResourceNotFoundException("Categoría no encontrada con ID: " + categoriaId);

        List<PersonNatural> personasNaturales = personaNaturalRepository.findByCategoriaPersonaId(categoriaId);
        return personasNaturales.stream().map(personaNaturalMapper::toResponseDTO).toList();
    }

    @Override
    public CategoryPersonaResponseDTO getCategoriaDePersona(Integer personaNaturalId) { 
        if (!personaNaturalRepository.existsById(personaNaturalId))
            throw new ResourceNotFoundException("Persona natural no encontrada con ID: " + personaNaturalId);

        PersonNatural personaNatural = personaNaturalRepository.findById(personaNaturalId).get();
        CategoryPerson categoriaPersona = personaNatural.getCategoryPerson();
        if (categoriaPersona == null)
            throw new ResourceNotFoundException("La persona natural con ID: " + personaNaturalId + " no tiene categoría asignada.");
            
        return categoriaPersonaMapper.toResponseDTO(categoriaPersona);
    }

    private List<CategoryPersonaResponseDTO> mapToResponseList(List<CategoryPerson> categorias) {
        return categorias.stream().map(categoriaPersonaMapper::toResponseDTO).toList();
    }
}