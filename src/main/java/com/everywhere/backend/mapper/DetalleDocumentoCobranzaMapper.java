package com.everywhere.backend.mapper;

import com.everywhere.backend.model.dto.DetailQuotationSimpleDTO;
import com.everywhere.backend.model.dto.DetailDocumentCollectionRequestDTO;
import com.everywhere.backend.model.dto.DetailDocumentCollectionResponseDTO;
import com.everywhere.backend.model.entity.DetailDocumentCollection;
import com.everywhere.backend.model.entity.DocumentCollection;
import com.everywhere.backend.model.entity.Product;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DetalleDocumentoCobranzaMapper {

    private final ModelMapper modelMapper;

    public DetailDocumentCollection toEntity(DetailDocumentCollectionRequestDTO detalleDocumentoCobranzaRequestDTO) {
        return modelMapper.map(detalleDocumentoCobranzaRequestDTO, DetailDocumentCollection.class);
    }

    public DetailDocumentCollectionResponseDTO toResponseDTO(DetailDocumentCollection detalleDocumentoCobranza) {
         DetailDocumentCollectionResponseDTO dto = modelMapper.map(detalleDocumentoCobranza, DetailDocumentCollectionResponseDTO.class);
         
        if (detalleDocumentoCobranza.getProduct() != null) {
            dto.setProductId(detalleDocumentoCobranza.getProduct().getId());
            dto.setProductDescription(detalleDocumentoCobranza.getProduct().getType());
        } 
        if (detalleDocumentoCobranza.getDocumentCollection() != null) {
            dto.setDocumentCollectionId(detalleDocumentoCobranza.getDocumentCollection().getId());
            dto.setDocumentCollectionNumber(
                String.format("%s-%09d", 
                    detalleDocumentoCobranza.getDocumentCollection().getSerie(),
                    detalleDocumentoCobranza.getDocumentCollection().getCorrelative()));
        }
        
        return dto;
    }

    public void updateEntityFromRequest(DetailDocumentCollection detalleDocumentoCobranza, DetailDocumentCollectionRequestDTO detalleDocumentoCobranzaRequestDTO) {
        modelMapper.map(detalleDocumentoCobranzaRequestDTO, detalleDocumentoCobranza);
    }

    //Convierte los detalles seleccionados de una cotización a detalles de documento de cobranza
    public List<DetailDocumentCollection> fromCotizacionDetalles(List<DetailQuotationSimpleDTO> detallesCotizacion, DocumentCollection documentoCobranza) {
        List<DetailDocumentCollection> detalles = new ArrayList<>();

        if (detallesCotizacion != null) {
            for (DetailQuotationSimpleDTO detalleCotizacionSimpleDTO : detallesCotizacion) {
                if (detalleCotizacionSimpleDTO.getSelected() != null && detalleCotizacionSimpleDTO.getSelected()) {
                    DetailDocumentCollection detalleDocumentoCobranza = new DetailDocumentCollection();
                    detalleDocumentoCobranza.setDocumentCollection(documentoCobranza);
                    detalleDocumentoCobranza.setAmount(detalleCotizacionSimpleDTO.getQuantity() != null ? detalleCotizacionSimpleDTO.getQuantity() : 0);
                    detalleDocumentoCobranza.setDescription(detalleCotizacionSimpleDTO.getDescription());
                    detalleDocumentoCobranza.setPrice(detalleCotizacionSimpleDTO.getPriceHistory() != null ? detalleCotizacionSimpleDTO.getPriceHistory() : BigDecimal.ZERO);

                    if (detalleCotizacionSimpleDTO.getProduct() != null) {
                        Product producto = new Product();
                        producto.setId(detalleCotizacionSimpleDTO.getProduct().getId());
                        detalleDocumentoCobranza.setProduct(producto);
                    }
                    detalles.add(detalleDocumentoCobranza);
                }
            }
        }
        return detalles;
    }
}