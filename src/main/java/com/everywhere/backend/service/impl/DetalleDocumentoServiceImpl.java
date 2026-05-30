package com.everywhere.backend.service.impl;

import com.everywhere.backend.exceptions.ResourceNotFoundException;
import com.everywhere.backend.mapper.DetalleDocumentoMapper;
import com.everywhere.backend.model.dto.DetailDocumentWithPersonDto;
import com.everywhere.backend.model.dto.DetailDocumentRequestDto;
import com.everywhere.backend.model.dto.DetailDocumentResponseDto;
import com.everywhere.backend.model.dto.DetailDocumentSearchDto;
import com.everywhere.backend.model.entity.DetailDocument;
import com.everywhere.backend.model.entity.PersonNatural;
import com.everywhere.backend.repository.DetalleDocumentoRepository;
import com.everywhere.backend.repository.DocumentoRepository;
import com.everywhere.backend.repository.PersonaNaturalRepository;
import com.everywhere.backend.service.DetalleDocumentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DetalleDocumentoServiceImpl implements DetalleDocumentoService {

    private final PersonaNaturalRepository personaNaturalRepository;
    private final DocumentoRepository documentoRepository;
    private final DetalleDocumentoRepository detalleDocumentoRepository;
    private final DetalleDocumentoMapper detalleDocumentoMapper;

    @Override
    public DetailDocumentResponseDto findById(Integer id) {
        DetailDocument detalle = detalleDocumentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DetalleDocumento no encontrado con id: " + id));
        return detalleDocumentoMapper.toResponse(detalle);
    }

    @Override
    @Transactional
    public DetailDocumentResponseDto save(DetailDocumentRequestDto detalleDocumentoRequestDto) {
        // Crear entidad manualmente sin usar el mapper para documento y personaNatural
        DetailDocument detalleDocumento = new DetailDocument();
        detalleDocumento.setNumber(detalleDocumentoRequestDto.getNumber());
        detalleDocumento.setDateIssue(detalleDocumentoRequestDto.getDateIssue());
        detalleDocumento.setDateExpiration(detalleDocumentoRequestDto.getDateExpiration());
        detalleDocumento.setOrigin(detalleDocumentoRequestDto.getOrigin());

        // Mapear Documento
        if (detalleDocumentoRequestDto.getDocumentId() != null) {
            if (!documentoRepository.existsById(detalleDocumentoRequestDto.getDocumentId()))
                throw new ResourceNotFoundException("Documento no encontrado con id: " + detalleDocumentoRequestDto.getDocumentId());
            detalleDocumento.setDocument(documentoRepository.findById(detalleDocumentoRequestDto.getDocumentId()).get());
        }

        // Mapear PersonaNatural y Viajero
        if (detalleDocumentoRequestDto.getPersonNaturalId() != null) {
            if (!personaNaturalRepository.existsById(detalleDocumentoRequestDto.getPersonNaturalId()))
                throw new ResourceNotFoundException("PersonaNatural no encontrado con id: " + detalleDocumentoRequestDto.getPersonNaturalId());

            PersonNatural personaNatural = personaNaturalRepository.findById(detalleDocumentoRequestDto.getPersonNaturalId()).get();
            detalleDocumento.setPersonNatural(personaNatural);

                    }

        return detalleDocumentoMapper.toResponse(detalleDocumentoRepository.save(detalleDocumento));
    }

    @Override
    @Transactional
    public DetailDocumentResponseDto update(Integer id, DetailDocumentRequestDto detalleDocumentoRequestDto) {
        if (!detalleDocumentoRepository.existsById(id))
            throw new ResourceNotFoundException("DetalleDocumento no encontrado con id: " + id);

        DetailDocument detalleDocumento = detalleDocumentoRepository.findById(id).get();

        // Actualizar campos simples
        detalleDocumento.setNumber(detalleDocumentoRequestDto.getNumber());
        detalleDocumento.setDateIssue(detalleDocumentoRequestDto.getDateIssue());
        detalleDocumento.setDateExpiration(detalleDocumentoRequestDto.getDateExpiration());
        detalleDocumento.setOrigin(detalleDocumentoRequestDto.getOrigin());

        // Mapear Documento
        if (detalleDocumentoRequestDto.getDocumentId() != null) {
            if (!documentoRepository.existsById(detalleDocumentoRequestDto.getDocumentId()))
                throw new ResourceNotFoundException("Documento no encontrado con id: " + detalleDocumentoRequestDto.getDocumentId());
            detalleDocumento.setDocument(documentoRepository.findById(detalleDocumentoRequestDto.getDocumentId()).get());
        }

        // Mapear PersonaNatural y Viajero
        if (detalleDocumentoRequestDto.getPersonNaturalId() != null) {
            if (!personaNaturalRepository.existsById(detalleDocumentoRequestDto.getPersonNaturalId()))
                throw new ResourceNotFoundException("PersonaNatural no encontrado con id: " + detalleDocumentoRequestDto.getPersonNaturalId());

            PersonNatural personaNatural = personaNaturalRepository.findById(detalleDocumentoRequestDto.getPersonNaturalId()).get();
            detalleDocumento.setPersonNatural(personaNatural);
        }

        return detalleDocumentoMapper.toResponse(detalleDocumentoRepository.save(detalleDocumento));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!detalleDocumentoRepository.existsById(id)) 
            throw new ResourceNotFoundException("DetalleDocumento no encontrado con id: " + id);
        detalleDocumentoRepository.deleteById(id);
    }

    @Override
    public List<DetailDocumentResponseDto> findAll() {
        return mapToResponseList(detalleDocumentoRepository.findAll());
    }

    @Override
    public List<DetailDocumentResponseDto> findByDocumentoId(Integer documentoId) {
        if (!documentoRepository.existsById(documentoId)) 
            throw new ResourceNotFoundException("Documento no encontrado con id: " + documentoId);
        return mapToResponseList(detalleDocumentoRepository.findByDocumentId(documentoId));
    }

    @Override
    public List<DetailDocumentResponseDto> findByNumero(String numero) {
        return mapToResponseList(detalleDocumentoRepository.findByNumberContainingIgnoreCase(numero));
    }

    @Override
    public List<DetailDocumentResponseDto> findByPersonaNaturalId(Integer personaNaturalId) {
        if (!personaNaturalRepository.existsById(personaNaturalId)) 
            throw new ResourceNotFoundException("PersonaNatural no encontrada con id: " + personaNaturalId);
        return mapToResponseList(detalleDocumentoRepository.findByPersonNaturalId(personaNaturalId));
    }

     @Override
    public List<DetailDocumentResponseDto> findByPersonaId(Integer personaId) {
        PersonNatural personaNatural = personaNaturalRepository.findByPersonId(personaId)
            .orElseThrow(() -> new ResourceNotFoundException("PersonaNatural no encontrada con personaId: " + personaId));
        return mapToResponseList(detalleDocumentoRepository.findByPersonNaturalId(personaNatural.getId()));
    }

    private List<DetailDocumentResponseDto> mapToResponseList(List<DetailDocument> detalles) {
        return detalles.stream().map(detalleDocumentoMapper::toResponse).toList();
    }

    @Override
    public List<DetailDocumentSearchDto> findByPersonaNaturalDocumentoPrefix(String prefijo) {
        if (prefijo == null || prefijo.trim().isEmpty()) {
            return new ArrayList<>();
        }
        List<DetailDocument> detalles = detalleDocumentoRepository.findByNumberStartingWithIgnoreCase(prefijo.trim());
        return detalles.stream()
                .map(detalle -> DetailDocumentSearchDto.builder()
                        .number(detalle.getNumber())
                        .personId(detalle.getPersonNatural() != null ? detalle.getPersonNatural().getPerson().getId() : null)
                        .name(detalle.getPersonNatural() != null ? detalle.getPersonNatural().getName() : null)
                        .surnamePaternal(detalle.getPersonNatural() != null ? detalle.getPersonNatural().getSurnamePaternal() : null)
                        .surnameMaternal(detalle.getPersonNatural() != null ? detalle.getPersonNatural().getSurnameMaternal() : null)
                        .sex(detalle.getPersonNatural() != null ? detalle.getPersonNatural().getSex() : null)
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<DetailDocumentWithPersonDto> findDocumentosConPersonas() {
        // Usar query optimizada con JOIN FETCH para evitar problema N+1
        List<DetailDocument> detalles = detalleDocumentoRepository.findAllWithPersonasAndDocumento();
        
        // Agrupar por número de documento (manejando números nulos)
        return detalles.stream()
                .filter(doc -> doc.getNumber() != null) // Filtrar documentos sin número
                .collect(Collectors.groupingBy(DetailDocument::getNumber))
                .entrySet().stream()
                .map(entry -> {
                    String numeroDocumento = entry.getKey();
                    List<DetailDocument> documentos = entry.getValue();
                    
                    // Obtener el tipo de documento (asumiendo que todos los documentos con el mismo número tienen el mismo tipo)
                    String tipoDocumento = documentos.isEmpty() || documentos.get(0).getDocument() == null 
                            ? "Sin tipo" 
                            : documentos.get(0).getDocument().getType();
                    
                    // Obtener la información de las personas (ID y nombre completo)
                    List<DetailDocumentWithPersonDto.PersonaInfo> personas = documentos.stream()
                            .filter(doc -> doc.getPersonNatural() != null)
                            .filter(doc -> doc.getPersonNatural().getPerson() != null)
                            .map(doc -> {
                                PersonNatural personaNatural = doc.getPersonNatural();
                                Integer personaId = personaNatural.getPerson().getId();
                                String nombreCompleto = String.format("%s %s %s", 
                                        personaNatural.getName() != null ? personaNatural.getName() : "",
                                        personaNatural.getSurnamePaternal() != null ? personaNatural.getSurnamePaternal() : "",
                                        personaNatural.getSurnameMaternal() != null ? personaNatural.getSurnameMaternal() : ""
                                ).trim();
                                return DetailDocumentWithPersonDto.PersonaInfo.builder()
                                        .personId(personaId)
                                        .nameComplete(nombreCompleto)
                                        .build();
                            })
                            .filter(p -> !p.getNameComplete().isEmpty())
                            .distinct()
                            .collect(Collectors.toList());
                    
                    return DetailDocumentWithPersonDto.builder()
                            .numberDocument(numeroDocumento)
                            .typeDocument(tipoDocumento)
                            .person(personas)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<DetailDocumentWithPersonDto> findDocumentosConPersonasByNumero(String numero) {
        if (numero == null || numero.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        // Usar query optimizada con JOIN FETCH para evitar problema N+1
        List<DetailDocument> detalles = detalleDocumentoRepository.findByNumberContainingWithPersonAndDocument(numero.trim());
        
        // Agrupar por número de documento (manejando números nulos)
        return detalles.stream()
                .filter(doc -> doc.getNumber() != null)
                .collect(Collectors.groupingBy(DetailDocument::getNumber))
                .entrySet().stream()
                .map(entry -> {
                    String numeroDocumento = entry.getKey();
                    List<DetailDocument> documentos = entry.getValue();
                    
                    // Obtener el tipo de documento
                    String tipoDocumento = documentos.isEmpty() || documentos.get(0).getDocument() == null 
                            ? "Sin tipo" 
                            : documentos.get(0).getDocument().getType();
                    
                    // Obtener la información de las personas (ID y nombre completo)
                    List<DetailDocumentWithPersonDto.PersonaInfo> personas = documentos.stream()
                            .filter(doc -> doc.getPersonNatural() != null)
                            .filter(doc -> doc.getPersonNatural().getPerson() != null)
                            .map(doc -> {
                                PersonNatural personaNatural = doc.getPersonNatural();
                                Integer personaId = personaNatural.getPerson().getId();
                                String nombreCompleto = String.format("%s %s %s", 
                                        personaNatural.getName() != null ? personaNatural.getName() : "",
                                        personaNatural.getSurnamePaternal() != null ? personaNatural.getSurnamePaternal() : "",
                                        personaNatural.getSurnameMaternal() != null ? personaNatural.getSurnameMaternal() : ""
                                ).trim();
                                return DetailDocumentWithPersonDto.PersonaInfo.builder()
                                        .personId(personaId)
                                        .nameComplete(nombreCompleto)
                                        .build();
                            })
                            .filter(p -> !p.getNameComplete().isEmpty())
                            .distinct()
                            .collect(Collectors.toList());
                    
                    return DetailDocumentWithPersonDto.builder()
                            .numberDocument(numeroDocumento)
                            .typeDocument(tipoDocumento)
                            .person(personas)
                            .build();
                })
                .collect(Collectors.toList());
    }
}