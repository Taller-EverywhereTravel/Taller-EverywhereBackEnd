package com.everywhere.backend.api;

import com.everywhere.backend.model.dto.PhonePersonRequestDTO;
import com.everywhere.backend.model.dto.PhonePersonResponseDTO;
import com.everywhere.backend.security.RequirePermission;
import com.everywhere.backend.service.TelefonoPersonaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/phone-person")
@RequiredArgsConstructor
public class    TelefonoPersonaController {

    private final TelefonoPersonaService telefonoPersonaService;

    @GetMapping("/person/{personaId}")
    @RequirePermission(module = "PERSONAS", permission = "READ")
    public ResponseEntity<List<PhonePersonResponseDTO>> findByPersonaId(@PathVariable Integer personaId) {
        return ResponseEntity.ok(telefonoPersonaService.findByPersonaId(personaId));
    }

    @GetMapping("/person/{personaId}/phone/{telefonoId}")
    @RequirePermission(module = "PERSONAS", permission = "READ")
    public ResponseEntity<PhonePersonResponseDTO> findById(@PathVariable Integer personaId, @PathVariable Integer telefonoId) {
        return telefonoPersonaService.findById(telefonoId, personaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/person/{personaId}")
    @RequirePermission(module = "PERSONAS", permission = "CREATE")
    public ResponseEntity<PhonePersonResponseDTO> create(@PathVariable Integer personaId,
                                                             @RequestBody @Valid PhonePersonRequestDTO telefonoPersonaRequestDTO) {
        return new ResponseEntity<>(telefonoPersonaService.save(telefonoPersonaRequestDTO, personaId), HttpStatus.CREATED);
    }

    @PatchMapping("/person/{personaId}/phone/{telefonoId}")
    @RequirePermission(module = "PERSONAS", permission = "UPDATE")
    public ResponseEntity<PhonePersonResponseDTO> update(@PathVariable Integer personaId,
                                                             @PathVariable Integer telefonoId,
                                                             @RequestBody  PhonePersonRequestDTO telefonoPersonaRequestDTO) {
        return ResponseEntity.ok(telefonoPersonaService.update(personaId, telefonoPersonaRequestDTO, telefonoId));
    }

    @DeleteMapping("/person/{personaId}/phone/{telefonoId}")
    @RequirePermission(module = "PERSONAS", permission = "DELETE")
    public ResponseEntity<Void> delete(@PathVariable Integer personaId, @PathVariable Integer telefonoId) {
        telefonoPersonaService.deleteById(telefonoId, personaId);
        return ResponseEntity.noContent().build();
    }
}
