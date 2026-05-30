package com.everywhere.backend.api;

import com.everywhere.backend.model.dto.PersonRequestDTO;
import com.everywhere.backend.model.dto.PersonResponseDTO;
import com.everywhere.backend.model.dto.PersonDisplayDto;
import com.everywhere.backend.security.RequirePermission;
import com.everywhere.backend.service.PersonaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonaController {

    private final PersonaService personaService;

    @GetMapping
    @RequirePermission(module = "PERSONAS", permission = "READ")
    public ResponseEntity<List<PersonResponseDTO>> getAllPersonas() { 
        return ResponseEntity.ok(personaService.findAll());
    }

    @GetMapping("/{id}")
    @RequirePermission(module = "PERSONAS", permission = "READ")
    public ResponseEntity<PersonResponseDTO> getPersonaById(@PathVariable Integer id) { 
        return ResponseEntity.ok(personaService.findById(id));
    }

    @GetMapping("/mail")
    @RequirePermission(module = "PERSONAS", permission = "READ")
    public ResponseEntity<List<PersonResponseDTO>> getPersonasByEmail(@RequestParam String email) { 
        return ResponseEntity.ok(personaService.findByEmail(email));
    }

    @GetMapping("/phone")
    @RequirePermission(module = "PERSONAS", permission = "READ")
    public ResponseEntity<List<PersonResponseDTO>> getPersonasByTelefono(@RequestParam String telefono) { 
        return ResponseEntity.ok(personaService.findByTelefono(telefono));
    }

    @PostMapping
    @RequirePermission(module = "PERSONAS", permission = "CREATE")
    public ResponseEntity<PersonResponseDTO> createPersona(@Valid @RequestBody PersonRequestDTO personaRequestDTO) { 
        return ResponseEntity.status(HttpStatus.CREATED).body(personaService.save(personaRequestDTO));
    }

    @PatchMapping("/{id}")
    @RequirePermission(module = "PERSONAS", permission = "UPDATE")
    public ResponseEntity<PersonResponseDTO> patchPersona(@PathVariable Integer id, @Valid @RequestBody PersonRequestDTO personaRequestDTO) { 
        return ResponseEntity.ok(personaService.patch(id, personaRequestDTO));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(module = "PERSONAS", permission = "DELETE")
    public ResponseEntity<Void> deletePersona(@PathVariable Integer id) {
        personaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{personaId}/NaturalOrJuridic")
    @RequirePermission(module = "PERSONAS", permission = "READ")
    public ResponseEntity<PersonDisplayDto> findPersonaNaturalOrJuridicaById(@PathVariable Integer personaId) {
        return ResponseEntity.ok(personaService.findPersonaNaturalOrJuridicaById(personaId));
    }
}