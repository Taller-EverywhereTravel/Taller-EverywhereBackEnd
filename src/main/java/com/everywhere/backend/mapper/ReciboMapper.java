package com.everywhere.backend.mapper;

import org.springframework.stereotype.Component;

import com.everywhere.backend.model.dto.QuotationWithDetailResponseDTO;
import com.everywhere.backend.model.dto.DetailReceiptResponseDTO;
import com.everywhere.backend.model.dto.ReceiptResponseDTO;
import com.everywhere.backend.model.dto.ReceiptUpdateDTO;
import com.everywhere.backend.model.entity.Quotation;
import com.everywhere.backend.model.entity.MethodPayment;
import com.everywhere.backend.model.entity.PersonJuridic;
import com.everywhere.backend.model.entity.PersonNatural;
import com.everywhere.backend.model.entity.Person;
import com.everywhere.backend.model.entity.Receipt;
import com.everywhere.backend.model.entity.Branch;
import com.everywhere.backend.repository.PersonaJuridicaRepository;
import com.everywhere.backend.repository.PersonaNaturalRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReciboMapper {
    
    private final ModelMapper modelMapper;
    private final PersonaNaturalRepository personaNaturalRepository;
    private final PersonaJuridicaRepository personaJuridicaRepository;
    private final DetalleReciboMapper detalleReciboMapper;

    @PostConstruct
    public void configureMapping() {
        modelMapper.typeMap(ReceiptUpdateDTO.class, Receipt.class).addMappings(mapper -> {
            mapper.skip(Receipt::setDetailDocument);
            mapper.skip(Receipt::setBranch);
            mapper.skip(Receipt::setPersonJuridic);
        });
    }

    // Mapea desde cotización a recibo
    public Receipt fromCotizacion(QuotationWithDetailResponseDTO cotizacionConDetallesResponseDTO,
            String serie, Integer correlativo) {
        Receipt recibo = new Receipt();

        recibo.setSerie(serie);
        recibo.setCorrelative(correlativo);
        recibo.setCurrency(cotizacionConDetallesResponseDTO.getCurrency());

        Quotation cotizacionEntity = new Quotation();
        cotizacionEntity.setId(cotizacionConDetallesResponseDTO.getId());
        recibo.setQuotation(cotizacionEntity);

        if (cotizacionConDetallesResponseDTO.getPerson() != null) {
            Person persona = new Person();
            persona.setId(cotizacionConDetallesResponseDTO.getPerson().getId());
            recibo.setPerson(persona);
        }

        if (cotizacionConDetallesResponseDTO.getBranch() != null) {
            Branch sucursal = new Branch();
            sucursal.setId(cotizacionConDetallesResponseDTO.getBranch().getId());
            recibo.setBranch(sucursal);
        }

        if (cotizacionConDetallesResponseDTO.getMethodPayment() != null) {
            MethodPayment formaPago = new MethodPayment();
            formaPago.setId(cotizacionConDetallesResponseDTO.getMethodPayment().getId());
            recibo.setMethodPayment(formaPago);
        }
        return recibo;
    }

    public void updateEntityFromUpdateDTO(Receipt recibo, ReceiptUpdateDTO reciboUpdateDTO) {
        modelMapper.map(reciboUpdateDTO, recibo);
    }

    public ReceiptResponseDTO toResponseDTO(Receipt recibo) {
        ReceiptResponseDTO reciboResponseDTO = modelMapper.map(recibo, ReceiptResponseDTO.class);

        // Mapear código de cotización
        if (recibo.getQuotation() != null) {
            reciboResponseDTO.setQuotationId(recibo.getQuotation().getId());
            if (recibo.getQuotation().getCodeQuotation() != null) {
                reciboResponseDTO.setCodeQuotation(recibo.getQuotation().getCodeQuotation());
            }
        }

        if (recibo.getDetailDocument() != null) {
            reciboResponseDTO.setDetailDocumentId(recibo.getDetailDocument().getId());
            reciboResponseDTO.setClientDocument(recibo.getDetailDocument().getNumber());
            if (recibo.getDetailDocument().getDocument() != null)
                reciboResponseDTO.setTypeDocumentClient(recibo.getDetailDocument().getDocument().getType());
        }

        // Siempre setear personaId si existe
        if (recibo.getPerson() != null) {
            reciboResponseDTO.setPersonId(recibo.getPerson().getId());
        }

        // PRIORIDAD 1: Si hay PersonaJuridica seleccionada, usar sus datos
        if (recibo.getPersonJuridic() != null) {
            PersonJuridic pj = recibo.getPersonJuridic();
            reciboResponseDTO.setPersonJuridicId(pj.getId());
            reciboResponseDTO.setPersonJuridicRuc(pj.getRuc());
            reciboResponseDTO.setPersonJuridicCompanyName(pj.getNameCompany());

            // Usar datos de PersonaJuridica para el cliente
            reciboResponseDTO.setClientName(pj.getNameCompany());
            reciboResponseDTO.setClientDocument(pj.getRuc());
            reciboResponseDTO.setTypeDocumentClient("RUC");
        }
        // PRIORIDAD 2: Si no hay PersonaJuridica, usar datos de Persona
        else if (recibo.getPerson() != null) {
            Integer personaId = recibo.getPerson().getId();

            PersonNatural personaNatural = personaNaturalRepository.findByPersonasId(personaId).orElse(null);
            if (personaNatural != null) {
                String nombreCompleto = String.join(" ",
                        personaNatural.getName() != null ? personaNatural.getName().trim() : "",
                        personaNatural.getSurnamePaternal() != null ? personaNatural.getSurnamePaternal().trim() : "",
                        personaNatural.getSurnameMaternal() != null ? personaNatural.getSurnameMaternal().trim() : "")
                        .trim();
                reciboResponseDTO.setClientName(nombreCompleto.isEmpty() ? "Sin nombre" : nombreCompleto);

                if (recibo.getDetailDocument() == null) {
                    reciboResponseDTO.setClientDocument(personaNatural.getDocument());
                    reciboResponseDTO.setTypeDocumentClient("DNI");
                }
            } else {
                PersonJuridic personaJuridica = personaJuridicaRepository.findByPersonasId(personaId).orElse(null);
                if (personaJuridica != null) {
                    reciboResponseDTO.setPersonJuridicId(personaJuridica.getId());
                    reciboResponseDTO.setPersonJuridicRuc(personaJuridica.getRuc());
                    reciboResponseDTO.setPersonJuridicCompanyName(personaJuridica.getNameCompany());

                    reciboResponseDTO.setClientName(personaJuridica.getNameCompany());
                    reciboResponseDTO.setClientDocument(personaJuridica.getRuc());
                    reciboResponseDTO.setTypeDocumentClient("RUC");
                }
            }
        }

        if (recibo.getBranch() != null) {
            reciboResponseDTO.setBranchId(recibo.getBranch().getId());
            reciboResponseDTO.setBranchDescription(recibo.getBranch().getDescription());
        }
        if (recibo.getMethodPayment() != null) {
            reciboResponseDTO.setMethodPaymentId(recibo.getMethodPayment().getId());
            reciboResponseDTO.setMethodPaymentDescription(recibo.getMethodPayment().getDescription());
        }

        // Mapear los detalles
        if (recibo.getDetailReceipt() != null && !recibo.getDetailReceipt().isEmpty()) {
            List<DetailReceiptResponseDTO> detallesDTO = recibo.getDetailReceipt().stream()
                    .map(detalleReciboMapper::toResponseDTO).toList();
            reciboResponseDTO.setDetail(detallesDTO);
        }
        
        return reciboResponseDTO;
    }
}
