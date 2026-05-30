package com.everywhere.backend.util.pdf;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Component;

import com.everywhere.backend.model.dto.DetailReceiptResponseDTO;
import com.everywhere.backend.model.dto.ReceiptResponseDTO;

/**
 * Generador de PDF para Recibo - Extiende PdfGenerator
 */
@Component
public class ReciboPdfGenerator extends PdfGenerator<ReceiptResponseDTO, DetailReceiptResponseDTO> {

    public ReciboPdfGenerator(NumberToTextConverter numberToTextConverter) {
        super(numberToTextConverter);
    }

    @Override
    protected String getDocumentTitle() {
        return "RECIBO";
    }

    @Override
    protected String getFooterText() {
        return "Representación Impresa de RECIBO";
    }

    @Override
    protected List<DetailReceiptResponseDTO> getDetalles(ReceiptResponseDTO documentoDTO) {
        return documentoDTO.getDetail();
    }

    @Override
    protected String getNumeroDocumento(ReceiptResponseDTO documentoDTO) {
        // Concatenar serie y correlativo para formar el número completo (ej:
        // R01-000000001)
        if (documentoDTO.getSerie() != null && documentoDTO.getCorrelative() != null) {
            return String.format("%s-%09d", documentoDTO.getSerie(), documentoDTO.getCorrelative());
        }
        return null;
    }

    @Override
    protected String getFechaEmision(ReceiptResponseDTO documentoDTO) {
        return documentoDTO.getDateIssue() != null
                ? documentoDTO.getDateIssue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : null;
    }

    @Override
    protected String getClienteNombre(ReceiptResponseDTO documentoDTO) {
        return documentoDTO.getClientName();
    }

    @Override
    protected String getClienteDocumento(ReceiptResponseDTO documentoDTO) {
        return documentoDTO.getClientDocument();
    }

    @Override
    protected String getTipoDocumentoCliente(ReceiptResponseDTO documentoDTO) {
        return documentoDTO.getTypeDocumentClient();
    }

    @Override
    protected String getSucursalDescripcion(ReceiptResponseDTO documentoDTO) {
        return documentoDTO.getBranchDescription();
    }

    @Override
    protected String getMoneda(ReceiptResponseDTO documentoDTO) {
        return documentoDTO.getCurrency();
    }

    @Override
    protected String getFileVenta(ReceiptResponseDTO documentoDTO) {
        return documentoDTO.getFileVenta();
    }

    @Override
    protected String getFormaPagoDescripcion(ReceiptResponseDTO documentoDTO) {
        return documentoDTO.getMethodPaymentDescription();
    }

    @Override
    protected String getObservaciones(ReceiptResponseDTO documentoDTO) {
        return documentoDTO.getObservation();
    }

    @Override
    protected BigDecimal getCostoEnvio(ReceiptResponseDTO documentoDTO) {
        // Recibo no tiene costo de envío, retorna 0
        return BigDecimal.ZERO;
    }

    @Override
    protected boolean showCostoEnvio() {
        // Recibo no muestra la fila de costo de envío
        return false;
    }

    @Override
    protected boolean showDisclaimer() {
        // Recibo no muestra el disclaimer de crédito fiscal
        return false;
    }

    @Override
    protected String getFechaVencimiento(ReceiptResponseDTO documentoDTO) {
        return documentoDTO.getDateExpiration() != null
                ? documentoDTO.getDateExpiration().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : null;
    }

    @Override
    protected Integer getCantidad(DetailReceiptResponseDTO detalle) {
        return detalle.getAmount();
    }

    @Override
    protected String getProductoDescripcion(DetailReceiptResponseDTO detalle) {
        return detalle.getProductDescription();
    }

    @Override
    protected String getDescripcionDetalle(DetailReceiptResponseDTO detalle) {
        return detalle.getDescription();
    }

    @Override
    protected BigDecimal getPrecio(DetailReceiptResponseDTO detalle) {
        return detalle.getPrice();
    }

    @Override
    protected Long getDetalleId(DetailReceiptResponseDTO detalle) {
        return detalle.getId();
    }
}
