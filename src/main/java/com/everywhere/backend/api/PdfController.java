package com.everywhere.backend.api;

import com.everywhere.backend.model.dto.DocumentCollectionResponseDTO;
import com.everywhere.backend.model.dto.ReceiptResponseDTO;
import com.everywhere.backend.security.RequirePermission;
import com.everywhere.backend.service.DocumentoCobranzaService;
import com.everywhere.backend.service.ReciboService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@RestController
@AllArgsConstructor
@RequestMapping("/pdf")
public class PdfController {

    private final DocumentoCobranzaService documentoCobranzaService;
    private final ReciboService reciboService;

    @GetMapping("/document-collection/{id}")
    @RequirePermission(module = "DOCUMENTOS_COBRANZA", permission = "READ")
    public ResponseEntity<InputStreamResource> generateDocumentoCobranzaPdf(@PathVariable Long id) {

        try {
            // Verificar que el documento existe usando el DTO
            DocumentCollectionResponseDTO documentoDto = documentoCobranzaService.findById(id);
            
            if (documentoDto == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            
            ByteArrayInputStream pdfStream = documentoCobranzaService.generatePdf(id);

            if (pdfStream == null) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            

            HttpHeaders headers = new HttpHeaders();
            String filename = String.format("%s-%09d", documentoDto.getSerie(), documentoDto.getCorrelative());
            headers.add("Content-Disposition", "inline; filename=" + filename + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(pdfStream));

        } catch (Exception e) {
            System.err.println("Error generando PDF: " + e.getMessage()); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/receipt/{id}")
    @RequirePermission(module = "DOCUMENTOS_COBRANZA", permission = "READ")
    public ResponseEntity<InputStreamResource> generateReciboPdf(@PathVariable Integer id) {

        try {
            // Verificar que el recibo existe usando el DTO
            ReceiptResponseDTO reciboDto = reciboService.findById(id);
            
            if (reciboDto == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            
            ByteArrayInputStream pdfStream = reciboService.generatePdf(id);

            if (pdfStream == null) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            
            HttpHeaders headers = new HttpHeaders();
            String filename = String.format("%s-%09d", reciboDto.getSerie(), reciboDto.getCorrelative());
            headers.add("Content-Disposition", "inline; filename=" + filename + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(pdfStream));

        } catch (Exception e) {
            System.err.println("Error generando PDF de recibo: " + e.getMessage()); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}