package com.everywhere.backend.api;

import com.everywhere.backend.model.dto.MailPersonRequestDTO;
import com.everywhere.backend.model.dto.MailPersonResponseDTO;
import com.everywhere.backend.security.RequirePermission;
import com.everywhere.backend.service.CorreoPersonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mail-person")
@RequiredArgsConstructor
public class CorreoPersonaController {

    private final CorreoPersonaService correoPersonaService;

    @GetMapping("/person/{personaId}")
    @RequirePermission(module = "PERSONAS", permission = "READ")
    public ResponseEntity<List<MailPersonResponseDTO>> findByPersonaId(@PathVariable Integer personaId) {
        return ResponseEntity.ok(correoPersonaService.findByPersonaId(personaId));
    }

    @GetMapping("/{correoId}")
    @RequirePermission(module = "PERSONAS", permission = "READ")
    public ResponseEntity<MailPersonResponseDTO> findById(@PathVariable Integer correoId) {
        return correoPersonaService.findById(correoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/person/{personaId}")
    @RequirePermission(module = "PERSONAS", permission = "CREATE")
    public ResponseEntity<MailPersonResponseDTO> save(
            @PathVariable Integer personaId,
            @RequestBody MailPersonRequestDTO correoPersonaRequestDTO) {
        return ResponseEntity.ok(correoPersonaService.save(correoPersonaRequestDTO, personaId));
    }

    @PatchMapping("/person/{personaId}/mail/{correoId}")
    @RequirePermission(module = "PERSONAS", permission = "UPDATE")
    public ResponseEntity<MailPersonResponseDTO> update(
            @PathVariable Integer personaId,
            @PathVariable Integer correoId,
            @RequestBody MailPersonRequestDTO correoPersonaRequestDTO) {
        return ResponseEntity.ok(correoPersonaService.update(personaId, correoPersonaRequestDTO, correoId));
    }

    @DeleteMapping("/mail/{correoId}")
    @RequirePermission(module = "PERSONAS", permission = "DELETE")
    public ResponseEntity<Void> deleteById(@PathVariable Integer correoId) {
        correoPersonaService.deleteById(correoId);
        return ResponseEntity.noContent().build();
    }
}
