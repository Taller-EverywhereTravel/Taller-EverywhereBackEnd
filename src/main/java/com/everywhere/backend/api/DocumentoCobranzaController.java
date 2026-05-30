package com.everywhere.backend.api;

import com.everywhere.backend.model.dto.DocumentCollectionResponseDTO;
import com.everywhere.backend.model.dto.DocumentCollectionUpdateDTO;
import com.everywhere.backend.security.RequirePermission;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.everywhere.backend.service.DocumentoCobranzaService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/document-collection")
public class DocumentoCobranzaController {

    private final DocumentoCobranzaService documentoCobranzaService;

    @PostMapping
    @RequirePermission(module = "DOCUMENTOS_COBRANZA", permission = "CREATE")
    public ResponseEntity<DocumentCollectionResponseDTO> createDocumentoCobranza(
            @RequestParam Integer cotizacionId,
            @RequestParam(required = false) Integer personaJuridicaId,
            @RequestParam(required = false) Integer sucursalId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(documentoCobranzaService.createDocumentoCobranza(cotizacionId, personaJuridicaId, sucursalId));
    }

    @GetMapping
    @RequirePermission(module = "DOCUMENTOS_COBRANZA", permission = "READ")
    public ResponseEntity<List<DocumentCollectionResponseDTO>> getAllDocumentos() {
        return ResponseEntity.ok(documentoCobranzaService.findAll());
    }

    @GetMapping("/{id}")
    @RequirePermission(module = "DOCUMENTOS_COBRANZA", permission = "READ")
    public ResponseEntity<?> getDocumentoById(@PathVariable Long id) {
        return ResponseEntity.ok(documentoCobranzaService.findById(id));
    }

    @PatchMapping("/{id}")
    @RequirePermission(module = "DOCUMENTOS_COBRANZA", permission = "UPDATE")
    public ResponseEntity<?> updateDocumento(@PathVariable Long id,
            @Valid @RequestBody DocumentCollectionUpdateDTO documentoCobranzaUpdateDTO) {
        return ResponseEntity.ok(documentoCobranzaService.patchDocumento(id, documentoCobranzaUpdateDTO));
    }

    // Endpoints para gestión de carpetas

    @GetMapping("/carpeta/{carpetaId}")
    @RequirePermission(module = "DOCUMENTOS_COBRANZA", permission = "READ")
    public ResponseEntity<List<DocumentCollectionResponseDTO>> findByCarpeta(@PathVariable Integer carpetaId) {
        return ResponseEntity.ok(documentoCobranzaService.findByCarpeta(carpetaId));
    }

    @GetMapping("/sin-carpeta")
    @RequirePermission(module = "DOCUMENTOS_COBRANZA", permission = "READ")
    public ResponseEntity<List<DocumentCollectionResponseDTO>> findSinCarpeta() {
        return ResponseEntity.ok(documentoCobranzaService.findSinCarpeta());
    }

    @PatchMapping("/{id}/carpeta")
    @RequirePermission(module = "DOCUMENTOS_COBRANZA", permission = "UPDATE")
    public ResponseEntity<DocumentCollectionResponseDTO> updateCarpeta(
            @PathVariable Long id,
            @RequestParam(required = false) Integer carpetaId) {
        return ResponseEntity.ok(documentoCobranzaService.updateCarpeta(id, carpetaId));
    }
}