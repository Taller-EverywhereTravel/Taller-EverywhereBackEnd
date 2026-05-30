package com.everywhere.backend.api;

import com.everywhere.backend.model.dto.NaturalJuridicRequestDTO;
import com.everywhere.backend.model.dto.NaturalJuridicResponseDTO;
import com.everywhere.backend.model.dto.NaturalJuridicPatchDTO;
import com.everywhere.backend.security.RequirePermission;
import com.everywhere.backend.service.NaturalJuridicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/natural-juridic")
@RequiredArgsConstructor
public class NaturalJuridicoController {

    private final NaturalJuridicoService naturalJuridicoService;

    @GetMapping
    @RequirePermission(module = "PERSONAS", permission = "READ")
    public ResponseEntity<List<NaturalJuridicResponseDTO>> getAllRelaciones() {
        return ResponseEntity.ok(naturalJuridicoService.findAll());
    }

    @GetMapping("/{id}")
    @RequirePermission(module = "PERSONAS", permission = "READ")
    public ResponseEntity<NaturalJuridicResponseDTO> getRelacionById(@PathVariable Integer id) { 
        return ResponseEntity.ok(naturalJuridicoService.findById(id));
    }

    @GetMapping("/person-natural/{personaNaturalId}")
    @RequirePermission(module = "PERSONAS", permission = "READ")
    public ResponseEntity<List<NaturalJuridicResponseDTO>> getRelacionesByPersonaNatural(
            @PathVariable Integer personaNaturalId) { 
        return ResponseEntity.ok(naturalJuridicoService.findByPersonaNaturalId(personaNaturalId));
    }

    @GetMapping("/person-juridic/{personaJuridicaId}")
    @RequirePermission(module = "PERSONAS", permission = "READ")
    public ResponseEntity<List<NaturalJuridicResponseDTO>> getRelacionesByPersonaJuridica(
            @PathVariable Integer personaJuridicaId) { 
        return ResponseEntity.ok(naturalJuridicoService.findByPersonaJuridicaId(personaJuridicaId));
    }

    @PostMapping
    @RequirePermission(module = "PERSONAS", permission = "CREATE")
    public ResponseEntity<List<NaturalJuridicResponseDTO>> crearRelaciones(
            @Valid @RequestBody NaturalJuridicRequestDTO naturalJuridicoRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(naturalJuridicoService.crearRelaciones(naturalJuridicoRequestDTO));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(module = "PERSONAS", permission = "DELETE")
    public ResponseEntity<Void> deleteRelacion(@PathVariable Integer id) {
        naturalJuridicoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/person-natural/{personaNaturalId}/person-juridic/{personaJuridicaId}")
    @RequirePermission(module = "PERSONAS", permission = "DELETE")
    public ResponseEntity<Void> deleteRelacionByPersonas(
            @PathVariable Integer personaNaturalId,
            @PathVariable Integer personaJuridicaId) {
        naturalJuridicoService.deleteByPersonas(personaNaturalId, personaJuridicaId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/person-natural/{personaNaturalId}")
    @RequirePermission(module = "PERSONAS", permission = "UPDATE")
    public ResponseEntity<List<NaturalJuridicResponseDTO>> patchRelacionesPersonaNatural(
            @PathVariable Integer personaNaturalId, @RequestBody NaturalJuridicPatchDTO naturalJuridicoPatchDTO) { 
        return ResponseEntity.ok(naturalJuridicoService.patchRelacionesPersonaNatural(personaNaturalId, naturalJuridicoPatchDTO));
    }
}