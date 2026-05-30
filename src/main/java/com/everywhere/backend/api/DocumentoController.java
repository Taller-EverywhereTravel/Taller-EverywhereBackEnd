package com.everywhere.backend.api;

import com.everywhere.backend.model.dto.DocumentRequestDto;
import com.everywhere.backend.model.dto.DocumentResponseDto;
import com.everywhere.backend.security.RequirePermission;
import com.everywhere.backend.service.DocumentoService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/document")
public class DocumentoController {

    private final DocumentoService documentoService;

    @GetMapping
    @RequirePermission(module = "DOCUMENTOS", permission = "CREATE")
    public ResponseEntity<List<DocumentResponseDto>> getAll() {
        return ResponseEntity.ok(documentoService.findAll());
    }

    @GetMapping("/{id}")
    @RequirePermission(module = "DOCUMENTOS", permission = "READ")
    public ResponseEntity<DocumentResponseDto> getById(@PathVariable int id) {
        return ResponseEntity.ok(documentoService.findById(id));
    }

    @PostMapping
    @RequirePermission(module = "DOCUMENTOS", permission = "CREATE")
    public ResponseEntity<DocumentResponseDto> create(@RequestBody DocumentRequestDto documentoRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(documentoService.create(documentoRequestDto));
    }

    @PatchMapping("/{id}")
    @RequirePermission(module = "DOCUMENTOS", permission = "UPDATE")
    public ResponseEntity<DocumentResponseDto> update(@PathVariable int id, @RequestBody DocumentRequestDto documentoRequestDto) {
        return ResponseEntity.ok(documentoService.patch(id, documentoRequestDto));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(module = "DOCUMENTOS", permission = "DELETE")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        documentoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}