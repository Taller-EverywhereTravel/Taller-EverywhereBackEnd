package com.everywhere.backend.util.pdf;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Component;

import com.everywhere.backend.model.dto.DetailDocumentCollectionResponseDTO;
import com.everywhere.backend.model.dto.DocumentCollectionResponseDTO;

/**
 * Generador de PDF para DocumentoCobranza - Extiende PdfGenerator
 */
@Component
public class DocumentoCobranzaPdfGenerator extends PdfGenerator<DocumentCollectionResponseDTO, DetailDocumentCollectionResponseDTO> {

    public DocumentoCobranzaPdfGenerator(NumberToTextConverter numberToTextConverter) {
        super(numberToTextConverter);
    }

    @Override
    protected String getDocumentTitle() {
        return "DOCUMENTO DE COBRANZA";
    }

    @Override
    protected String getFooterText() {
        return "Representación Impresa de DOCUMENTO DE COBRANZA";
    }

    @Override
    protected List<DetailDocumentCollectionResponseDTO> getDetalles(DocumentCollectionResponseDTO documentoDTO) {
        return documentoDTO.getDetail();
    }

    @Override
    protected String getNumeroDocumento(DocumentCollectionResponseDTO documentoDTO) {
        // Concatenar serie y correlativo para formar el número completo (ej: DC01-000000001)
        if (documentoDTO.getSerie() != null && documentoDTO.getCorrelative() != null) {
            return String.format("%s-%09d", documentoDTO.getSerie(), documentoDTO.getCorrelative());
        }
        return null;
    }

    @Override
    protected String getFechaEmision(DocumentCollectionResponseDTO documentoDTO) {
        return documentoDTO.getDateIssue() != null 
            ? documentoDTO.getDateIssue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) 
            : null;
    }

    @Override
    protected String getClienteNombre(DocumentCollectionResponseDTO documentoDTO) {
        return documentoDTO.getClientName();
    }

    @Override
    protected String getClienteDocumento(DocumentCollectionResponseDTO documentoDTO) {
        return documentoDTO.getClientDocument();
    }

    @Override
    protected String getTipoDocumentoCliente(DocumentCollectionResponseDTO documentoDTO) {
        return documentoDTO.getTypeDocumentClient();
    }

    @Override
    protected String getSucursalDescripcion(DocumentCollectionResponseDTO documentoDTO) {
        return documentoDTO.getBranchDescription();
    }

    @Override
    protected String getMoneda(DocumentCollectionResponseDTO documentoDTO) {
        return documentoDTO.getCurrency();
    }

    @Override
    protected String getFileVenta(DocumentCollectionResponseDTO documentoDTO) {
        return documentoDTO.getFileVenta();
    }

    @Override
    protected String getFormaPagoDescripcion(DocumentCollectionResponseDTO documentoDTO) {
        return documentoDTO.getMethodPaymentDescription();
    }

    @Override
    protected String getObservaciones(DocumentCollectionResponseDTO documentoDTO) {
        return documentoDTO.getObservationes();
    }

    @Override
    protected BigDecimal getCostoEnvio(DocumentCollectionResponseDTO documentoDTO) {
        return documentoDTO.getCostShipping();
    }

    @Override
    protected Integer getCantidad(DetailDocumentCollectionResponseDTO detalle) {
        return detalle.getQuantity();
    }

    @Override
    protected String getProductoDescripcion(DetailDocumentCollectionResponseDTO detalle) {
        return detalle.getProductDescription();
    }

    @Override
    protected String getDescripcionDetalle(DetailDocumentCollectionResponseDTO detalle) {
        return detalle.getDescription();
    }

    @Override
    protected BigDecimal getPrecio(DetailDocumentCollectionResponseDTO detalle) {
        return detalle.getPrice();
    }

    @Override
    protected Long getDetalleId(DetailDocumentCollectionResponseDTO detalle) {
        return detalle.getId();
    }
}
