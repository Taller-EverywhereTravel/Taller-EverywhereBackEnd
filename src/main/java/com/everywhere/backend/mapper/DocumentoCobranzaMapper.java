package com.everywhere.backend.mapper;

import com.everywhere.backend.model.dto.QuotationWithDetailResponseDTO;
import com.everywhere.backend.model.dto.DetailDocumentCollectionResponseDTO;
import com.everywhere.backend.model.dto.DocumentCollectionResponseDTO;
import com.everywhere.backend.model.dto.DocumentCollectionUpdateDTO;
import com.everywhere.backend.model.entity.Quotation;
import com.everywhere.backend.model.entity.DocumentCollection;
import com.everywhere.backend.model.entity.MethodPayment;
import com.everywhere.backend.model.entity.PersonJuridic;
import com.everywhere.backend.model.entity.PersonNatural;
import com.everywhere.backend.model.entity.Person;
import com.everywhere.backend.model.entity.Branch;
import com.everywhere.backend.repository.PersonaJuridicaRepository;
import com.everywhere.backend.repository.PersonaNaturalRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DocumentoCobranzaMapper {

    private final ModelMapper modelMapper;
    private final PersonaNaturalRepository personaNaturalRepository;
    private final PersonaJuridicaRepository personaJuridicaRepository;
    private final DetalleDocumentoCobranzaMapper detalleDocumentoCobranzaMapper;

    @PostConstruct
    public void configureMapping() {
        modelMapper.typeMap(DocumentCollectionUpdateDTO.class, DocumentCollection.class).addMappings(mapper -> {
            mapper.skip(DocumentCollection::setDetailDocument);
            mapper.skip(DocumentCollection::setBranch);
            mapper.skip(DocumentCollection::setPersonJuridic);
        });
    }

    // maneja la lógica de mapeo desde cotización a documento de cobranza
    public DocumentCollection fromCotizacion(QuotationWithDetailResponseDTO cotizacionConDetallesResponseDTO,
            String serie, Integer correlativo) {
        DocumentCollection documentoCobranza = new DocumentCollection();

        documentoCobranza.setSerie(serie);
        documentoCobranza.setCorrelative(correlativo);
        documentoCobranza.setCurrency(cotizacionConDetallesResponseDTO.getCurrency());

        Quotation cotizacionEntity = new Quotation();
        cotizacionEntity.setId(cotizacionConDetallesResponseDTO.getId());
        documentoCobranza.setQuotation(cotizacionEntity);

        if (cotizacionConDetallesResponseDTO.getPerson() != null) {
            Person persona = new Person();
            persona.setId(cotizacionConDetallesResponseDTO.getPerson().getId());
            documentoCobranza.setPerson(persona);
        }

        if (cotizacionConDetallesResponseDTO.getBranch() != null) {
            Branch sucursal = new Branch();
            sucursal.setId(cotizacionConDetallesResponseDTO.getBranch().getId());
            documentoCobranza.setBranch(sucursal);
        }

        if (cotizacionConDetallesResponseDTO.getMethodPayment() != null) {
            MethodPayment formaPago = new MethodPayment();
            formaPago.setId(cotizacionConDetallesResponseDTO.getMethodPayment().getId());
            documentoCobranza.setMethodPayment(formaPago);
        }
        return documentoCobranza;
    }

    public void updateEntityFromUpdateDTO(DocumentCollection documentoCobranza,
            DocumentCollectionUpdateDTO documentoCobranzaUpdateDTO) {
        modelMapper.map(documentoCobranzaUpdateDTO, documentoCobranza);
    }

    public DocumentCollectionResponseDTO toResponseDTO(DocumentCollection documentoCobranza) {
        DocumentCollectionResponseDTO documentoCobranzaResponseDTO = modelMapper.map(documentoCobranza,
                DocumentCollectionResponseDTO.class);

        // Mapear código de cotización
        if (documentoCobranza.getQuotation() != null) {
            documentoCobranzaResponseDTO.setQuotationId(documentoCobranza.getQuotation().getId());
            if (documentoCobranza.getQuotation().getCodeQuotation() != null) {
                documentoCobranzaResponseDTO
                        .setCodeQuotation(documentoCobranza.getQuotation().getCodeQuotation());
            }
        }

        if (documentoCobranza.getDetailDocument() != null) {
            documentoCobranzaResponseDTO.setDetailDocumentId(documentoCobranza.getDetailDocument().getId());
            documentoCobranzaResponseDTO.setClientDocument(documentoCobranza.getDetailDocument().getNumber());
            if (documentoCobranza.getDetailDocument().getDocument() != null)
                documentoCobranzaResponseDTO
                        .setTypeDocumentClient(documentoCobranza.getDetailDocument().getDocument().getType());
        }

        // Siempre setear personaId si existe
        if (documentoCobranza.getPerson() != null) {
            documentoCobranzaResponseDTO.setPersonId(documentoCobranza.getPerson().getId());
        }

        // PRIORIDAD 1: Si hay PersonaJuridica seleccionada, usar sus datos
        if (documentoCobranza.getPersonJuridic() != null) {
            PersonJuridic pj = documentoCobranza.getPersonJuridic();
            documentoCobranzaResponseDTO.setPersonJuridicId(pj.getId());
            documentoCobranzaResponseDTO.setPersonJuridicRuc(pj.getRuc());
            documentoCobranzaResponseDTO.setPersonJuridicNameCompany(pj.getNameCompany());

            // Usar datos de PersonaJuridica para el cliente
            documentoCobranzaResponseDTO.setClientName(pj.getNameCompany()); // Señores: razón social de la empresa
            documentoCobranzaResponseDTO.setClientDocument(pj.getRuc()); // Documento: RUC de la empresa
            documentoCobranzaResponseDTO.setTypeDocumentClient("RUC");
        }
        // PRIORIDAD 2: Si no hay PersonaJuridica, usar datos de Persona (Natural o
        // Jurídica base)
        else if (documentoCobranza.getPerson() != null) {
            Integer personaId = documentoCobranza.getPerson().getId();

            PersonNatural personaNatural = personaNaturalRepository.findByPersonasId(personaId).orElse(null);
            if (personaNatural != null) {
                // Concatenación null-safe para evitar mostrar "null" en el nombre
                String nombreCompleto = String.join(" ",
                        personaNatural.getName() != null ? personaNatural.getName().trim() : "",
                        personaNatural.getSurnamePaternal() != null ? personaNatural.getSurnamePaternal().trim() : "",
                        personaNatural.getSurnameMaternal() != null ? personaNatural.getSurnameMaternal().trim() : "")
                        .trim();
                documentoCobranzaResponseDTO.setClientName(nombreCompleto.isEmpty() ? "Sin nombre" : nombreCompleto);

                // Si no hay detalleDocumento seleccionado, usar el campo documento de
                // PersonaNatural (legacy)
                if (documentoCobranza.getDetailDocument() == null) {
                    documentoCobranzaResponseDTO.setClientDocument(personaNatural.getDocument());
                    documentoCobranzaResponseDTO.setTypeDocumentClient("DNI");
                }
            } else {
                PersonJuridic personaJuridica = personaJuridicaRepository.findByPersonasId(personaId).orElse(null);
                if (personaJuridica != null) {
                    documentoCobranzaResponseDTO.setPersonJuridicId(personaJuridica.getId());
                    documentoCobranzaResponseDTO.setPersonJuridicRuc(personaJuridica.getRuc());
                    documentoCobranzaResponseDTO.setPersonJuridicNameCompany(personaJuridica.getNameCompany());

                    documentoCobranzaResponseDTO.setClientName(personaJuridica.getNameCompany()); // Señores: razón  social de la empresa
                    documentoCobranzaResponseDTO.setClientDocument(personaJuridica.getRuc()); // Documento: RUC de la empresa
                    documentoCobranzaResponseDTO.setTypeDocumentClient("RUC");
                }
            }
        }

        if (documentoCobranza.getBranch() != null) {
            documentoCobranzaResponseDTO.setBranchId(documentoCobranza.getBranch().getId());
            documentoCobranzaResponseDTO.setBranchDescription(documentoCobranza.getBranch().getDescription());
        }
        if (documentoCobranza.getMethodPayment() != null) {
            documentoCobranzaResponseDTO.setMethodPaymentId(documentoCobranza.getMethodPayment().getId());
            documentoCobranzaResponseDTO.setMethodPaymentDescription(documentoCobranza.getMethodPayment().getDescription());
        }

        // Mapear los detalles con el DetalleDocumentoCobranzaMapper
        if (documentoCobranza.getDetail() != null && !documentoCobranza.getDetail().isEmpty()) {
            List<DetailDocumentCollectionResponseDTO> detallesDTO = documentoCobranza.getDetail().stream()
                    .map(detalleDocumentoCobranzaMapper::toResponseDTO).toList();
            documentoCobranzaResponseDTO.setDetail(detallesDTO);

            System.out.println("Mapeados " + detallesDTO.size() + " detalles para documento " + documentoCobranza.getId());
        }
        return documentoCobranzaResponseDTO;
    }
}