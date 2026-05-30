package com.everywhere.backend.api;

import com.everywhere.backend.model.dto.DetailDocumentWithPersonDto;
import com.everywhere.backend.model.dto.DetailDocumentResponseDto;
import com.everywhere.backend.model.dto.DetailDocumentRequestDto;
import com.everywhere.backend.model.dto.DetailDocumentSearchDto;
import com.everywhere.backend.service.DetalleDocumentoService;
import com.everywhere.backend.security.RequirePermission;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detail-document")
@RequiredArgsConstructor
public class DetalleDocumentoController {

    private final DetalleDocumentoService detalleDocumentoService;

    @GetMapping
    @RequirePermission(module = "DOCUMENTOS", permission = "READ")
    public ResponseEntity<List<DetailDocumentResponseDto>> findAll() {
        return ResponseEntity.ok(detalleDocumentoService.findAll());
    }

    @GetMapping("/person/{personaId}")
    @RequirePermission(module = "DOCUMENTOS", permission = "READ")
    public ResponseEntity<List<DetailDocumentResponseDto>> findByPersonaId(@PathVariable Integer personaId) {
        return ResponseEntity.ok(detalleDocumentoService.findByPersonaId(personaId));
    }

    @GetMapping("/{id}")
    @RequirePermission(module = "DOCUMENTOS", permission = "READ")
    public ResponseEntity<DetailDocumentResponseDto> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(detalleDocumentoService.findById(id));
    }

    @GetMapping("/document/{documentoId}")
    @RequirePermission(module = "DOCUMENTOS", permission = "READ")
    public ResponseEntity<List<DetailDocumentResponseDto>> findByDocumentoId(@PathVariable Integer documentoId) {
        return ResponseEntity.ok(detalleDocumentoService.findByDocumentoId(documentoId));
    }

    @GetMapping("/number/{numero}")
    @RequirePermission(module = "DOCUMENTOS", permission = "READ")
    public ResponseEntity<List<DetailDocumentResponseDto>> findByNumero(@PathVariable String numero) {
        return ResponseEntity.ok(detalleDocumentoService.findByNumero(numero));
    }

    @GetMapping("/person-natural/{personaNaturalId}")
    @RequirePermission(module = "DOCUMENTOS", permission = "READ")
    public ResponseEntity<List<DetailDocumentResponseDto>> findByPersonaNaturalId(
            @PathVariable Integer personaNaturalId) {
        return ResponseEntity.ok(detalleDocumentoService.findByPersonaNaturalId(personaNaturalId));
    }

    @PostMapping
    @RequirePermission(module = "DOCUMENTOS", permission = "CREATE")
    public ResponseEntity<DetailDocumentResponseDto> save(
            @Valid @RequestBody DetailDocumentRequestDto detalleDocumentoRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(detalleDocumentoService.save(detalleDocumentoRequestDto));
    }

    @PatchMapping("/{id}")
    @RequirePermission(module = "DOCUMENTOS", permission = "UPDATE")
    public ResponseEntity<DetailDocumentResponseDto> update(@PathVariable Integer id,
            @Valid @RequestBody DetailDocumentRequestDto detalleDocumentoRequestDto) {
        return ResponseEntity.ok(detalleDocumentoService.update(id, detalleDocumentoRequestDto));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(module = "VIAJEROS", permission = "DELETE")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        detalleDocumentoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/person-natural/document-number")
    @RequirePermission(module = "DOCUMENTOS", permission = "READ")
    public ResponseEntity<List<DetailDocumentSearchDto>> findByPersonaNaturalDocumentoPrefix(
            @RequestParam(name = "prefijo") String prefijo) {
        return ResponseEntity.ok(detalleDocumentoService.findByPersonaNaturalDocumentoPrefix(prefijo));
    }

    @GetMapping("/document-with-person")
    @RequirePermission(module = "DOCUMENTOS", permission = "READ")
    public ResponseEntity<List<DetailDocumentWithPersonDto>> findDocumentosConPersonas() {
        return ResponseEntity.ok(detalleDocumentoService.findDocumentosConPersonas());
    }

    @GetMapping("/search-by-number")
    @RequirePermission(module = "DOCUMENTOS", permission = "READ")
    public ResponseEntity<List<DetailDocumentWithPersonDto>> findDocumentosConPersonasByNumero(
            @RequestParam(name = "numero") String numero) {
        return ResponseEntity.ok(detalleDocumentoService.findDocumentosConPersonasByNumero(numero));
    }
}