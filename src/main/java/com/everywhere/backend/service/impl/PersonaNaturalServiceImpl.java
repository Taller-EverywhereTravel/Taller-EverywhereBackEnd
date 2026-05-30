package com.everywhere.backend.service.impl;

import com.everywhere.backend.model.dto.PersonNaturalRequestDTO;
import com.everywhere.backend.model.dto.PersonNaturalResponseDTO;
import com.everywhere.backend.model.entity.PersonNatural;
import com.everywhere.backend.model.entity.Traveler;
import com.everywhere.backend.model.entity.Person;
import com.everywhere.backend.model.entity.CategoryPerson;
import com.everywhere.backend.repository.PersonaNaturalRepository;
import com.everywhere.backend.repository.ViajeroRepository;
import com.everywhere.backend.repository.PersonaRepository;
import com.everywhere.backend.repository.CategoriaPersonaRepository;
import com.everywhere.backend.service.PersonaNaturalService;

import com.everywhere.backend.exceptions.ResourceNotFoundException;
import com.everywhere.backend.exceptions.BadRequestException;
import com.everywhere.backend.mapper.PersonaNaturalMapper;
import com.everywhere.backend.mapper.PersonaMapper;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonaNaturalServiceImpl implements PersonaNaturalService {

    private final PersonaNaturalRepository personaNaturalRepository;
    private final PersonaRepository personaRepository;
    private final ViajeroRepository viajeroRepository;
    private final CategoriaPersonaRepository categoriaPersonaRepository;
    private final PersonaNaturalMapper personaNaturalMapper;
    private final PersonaMapper personaMapper;

    @Override
    public List<PersonNaturalResponseDTO> findAll() {
        return personaNaturalRepository.findAll().stream().map(personaNaturalMapper::toResponseDTO).toList();
    }

    @Override
    public PersonNaturalResponseDTO findById(Integer id) {
        PersonNatural personaNatural = personaNaturalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Persona natural no encontrada con ID: " + id));
        return personaNaturalMapper.toResponseDTO(personaNatural);
    }

    @Override
    public List<PersonNaturalResponseDTO> findByDocumento(String documento) {
        Optional<PersonNatural> personaNaturalOptional = personaNaturalRepository.findByDocumentoIgnoreCase(documento);
        if (personaNaturalOptional.isEmpty())
            return List.of();
        return List.of(personaNaturalMapper.toResponseDTO(personaNaturalOptional.get()));
    }

    @Override
    public List<PersonNaturalResponseDTO> findByNombres(String nombres) {
        List<PersonNatural> personaNaturalList = personaNaturalRepository.findByNombresIgnoreAccents(nombres);
        return personaNaturalList.stream().map(personaNaturalMapper::toResponseDTO).toList();
    }

    @Override
    public List<PersonNaturalResponseDTO> findByApellidosPaternos(String apellidosPaternos) {
        List<PersonNatural> personaNaturalList = personaNaturalRepository.findByApellidosPaternoIgnoreAccents(apellidosPaternos);
        if (personaNaturalList.isEmpty())
            throw new ResourceNotFoundException("No se encontraron personas naturales con apellidos paternos: " + apellidosPaternos);
        return personaNaturalList.stream().map(personaNaturalMapper::toResponseDTO).toList();
    }

    @Override
    public List<PersonNaturalResponseDTO> findByApellidosMaternos(String apellidosMaternos) {
        List<PersonNatural> personaNaturalList = personaNaturalRepository.findByApellidosMaternoIgnoreAccents(apellidosMaternos);
        if (personaNaturalList.isEmpty())
            throw new ResourceNotFoundException("No se encontraron personas naturales con apellidos maternos: " + apellidosMaternos);
        return personaNaturalList.stream().map(personaNaturalMapper::toResponseDTO).toList();
    }

    @Override
    public PersonNaturalResponseDTO save(PersonNaturalRequestDTO personaNaturalRequestDTO) {
        // Validar que no exista ya una persona con el mismo documento
        if (personaNaturalRequestDTO.getDocument() != null && !personaNaturalRequestDTO.getDocument().trim().isEmpty()) {
            if (personaNaturalRepository.findByDocumentoIgnoreCase(personaNaturalRequestDTO.getDocument()).isPresent())
                throw new DataIntegrityViolationException("Ya existe una persona natural con el documento: " + personaNaturalRequestDTO.getDocument());
        }

        // Crear la persona base
        Person persona = (personaNaturalRequestDTO.getPerson() != null)
            ? personaMapper.toEntity(personaNaturalRequestDTO.getPerson())
            : new Person();

        // Crear la persona natural
        PersonNatural personaNatural = personaNaturalMapper.toEntity(personaNaturalRequestDTO);
        personaNatural.setPerson(personaRepository.save(persona)); 

        if (personaNaturalRequestDTO.getTravelerId() != null) {
            Traveler viajero = viajeroRepository.findById(personaNaturalRequestDTO.getTravelerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Viajero no encontrado con ID: " + personaNaturalRequestDTO.getTravelerId()));
            personaNatural.setTraveler(viajero);
        }

        if (personaNaturalRequestDTO.getCategoryPersonId() != null) {
            CategoryPerson categoria = categoriaPersonaRepository.findById(personaNaturalRequestDTO.getCategoryPersonId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + personaNaturalRequestDTO.getCategoryPersonId()));
            personaNatural.setCategoryPerson(categoria);
        } 
        return personaNaturalMapper.toResponseDTO(personaNaturalRepository.save(personaNatural));
    }

    @Override
    public PersonNaturalResponseDTO patch(Integer id, PersonNaturalRequestDTO personaNaturalRequestDTO) {
        // 🚀 OPTIMIZACIÓN 1: Validar existencia ANTES de buscar el objeto
        if (!personaNaturalRepository.existsById(id))
            throw new ResourceNotFoundException("Persona natural no encontrada con ID: " + id);

        // 🚀 OPTIMIZACIÓN 2: Si viene documento, validar duplicado ANTES de buscar el objeto completo
        if (personaNaturalRequestDTO.getDocument() != null && 
            !personaNaturalRequestDTO.getDocument().trim().isEmpty() &&
            personaNaturalRepository.findByDocumentoIgnoreCaseAndIdNot(personaNaturalRequestDTO.getDocument(), id).isPresent()) {
            throw new BadRequestException("Ya existe otra persona natural con el documento: " + personaNaturalRequestDTO.getDocument());
        }

        // 🚀 OPTIMIZACIÓN 3: Si viene categoría, validar existencia ANTES de buscar objetos
        if (personaNaturalRequestDTO.getCategoryPersonId() != null && 
            !categoriaPersonaRepository.existsById(personaNaturalRequestDTO.getCategoryPersonId())) {
            throw new ResourceNotFoundException("Categoría no encontrada con ID: " + personaNaturalRequestDTO.getCategoryPersonId());
        }

        // Solo ahora buscar los objetos para hacer el update
        PersonNatural existingPersonaNatural = personaNaturalRepository.findById(id).get();
        personaNaturalMapper.updateEntityFromDTO(personaNaturalRequestDTO, existingPersonaNatural);
        
        // Manejar categoría si se proporciona
        if (personaNaturalRequestDTO.getCategoryPersonId() != null) {
            CategoryPerson categoria = categoriaPersonaRepository.findById(personaNaturalRequestDTO.getCategoryPersonId()).get();
            existingPersonaNatural.setCategoryPerson(categoria);
        } 
        return personaNaturalMapper.toResponseDTO(personaNaturalRepository.save(existingPersonaNatural));
    }

    @Override
    public void deleteById(Integer id) {
        if (!personaNaturalRepository.existsById(id))
            throw new ResourceNotFoundException("Persona natural no encontrada con ID: " + id);
        personaNaturalRepository.deleteById(id);
    }

    @Override
    @Transactional
    public PersonNaturalResponseDTO asociarViajero(Integer personaNaturalId, Integer viajeroId) {
        PersonNatural personaNatural = personaNaturalRepository.findById(personaNaturalId)
                .orElseThrow(() -> new ResourceNotFoundException("Persona natural no encontrada con ID: " + personaNaturalId));

        Traveler viajero = viajeroRepository.findById(viajeroId)
                .orElseThrow(() -> new ResourceNotFoundException("Viajero no encontrado con ID: " + viajeroId));

        personaNatural.setTraveler(viajero);
        return personaNaturalMapper.toResponseDTO(personaNaturalRepository.save(personaNatural));
    }

    @Override
    @Transactional
    public PersonNaturalResponseDTO desasociarViajero(Integer personaNaturalId) {
        PersonNatural personaNatural = personaNaturalRepository.findById(personaNaturalId)
                .orElseThrow(() -> new ResourceNotFoundException("Persona natural no encontrada con ID: " + personaNaturalId));

        personaNatural.setTraveler(null); 
        return personaNaturalMapper.toResponseDTO(personaNaturalRepository.save(personaNatural));
    }
}