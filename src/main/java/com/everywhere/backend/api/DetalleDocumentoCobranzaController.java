package com.everywhere.backend.api;

import com.everywhere.backend.model.dto.DetailDocumentCollectionRequestDTO;
import com.everywhere.backend.model.dto.DetailDocumentCollectionResponseDTO;
import com.everywhere.backend.security.RequirePermission;
import com.everywhere.backend.service.DetalleDocumentoCobranzaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detail-document-collection")
@RequiredArgsConstructor
public class DetalleDocumentoCobranzaController {

    private final DetalleDocumentoCobranzaService detalleService;

    @GetMapping
    @RequirePermission(module = "DOCUMENTOS_COBRANZA", permission = "READ")
    public ResponseEntity<List<DetailDocumentCollectionResponseDTO>> getAllDetalles() { 
        return ResponseEntity.ok(detalleService.findAll());
    }

    @GetMapping("/{id}")
    @RequirePermission(module = "DOCUMENTOS_COBRANZA", permission = "READ")
    public ResponseEntity<DetailDocumentCollectionResponseDTO> getDetalleById(@PathVariable Long id) {
        return ResponseEntity.ok(detalleService.findById(id));
    }

    @GetMapping("/document-collection/{documentoId}")
    @RequirePermission(module = "DOCUMENTOS_COBRANZA", permission = "READ")
    public ResponseEntity<List<DetailDocumentCollectionResponseDTO>> getDetallesByDocumentoCobranza(@PathVariable Long documentoId) {
        return ResponseEntity.ok(detalleService.findByDocumentoCobranzaId(documentoId));
    }

    @PostMapping
    @RequirePermission(module = "DOCUMENTOS_COBRANZA", permission = "CREATE")
    public ResponseEntity<DetailDocumentCollectionResponseDTO> createDetalle(@Valid @RequestBody DetailDocumentCollectionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(detalleService.save(dto));
    }

    @PatchMapping("/{id}")
    @RequirePermission(module = "DOCUMENTOS_COBRANZA", permission = "UPDATE")
    public ResponseEntity<DetailDocumentCollectionResponseDTO> updateDetalle(
            @PathVariable Long id, @Valid @RequestBody DetailDocumentCollectionRequestDTO detalleDocumentoCobranzaRequestDTO) { 
        return ResponseEntity.ok(detalleService.patch(id, detalleDocumentoCobranzaRequestDTO));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(module = "DOCUMENTOS_COBRANZA", permission = "DELETE")
    public ResponseEntity<Void> deleteDetalle(@PathVariable Long id) {
        detalleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}