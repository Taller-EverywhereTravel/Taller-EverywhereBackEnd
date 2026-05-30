package com.everywhere.backend.api;

import com.everywhere.backend.model.dto.PersonJuridicRequestDTO;
import com.everywhere.backend.model.dto.PersonJuridicResponseDTO;
import com.everywhere.backend.security.RequirePermission;
import com.everywhere.backend.service.PersonaJuridicaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person-juridic")
@RequiredArgsConstructor
public class PersonaJuridicaController {

    private final PersonaJuridicaService personaJuridicaService;

    @GetMapping
    @RequirePermission(module = "PERSONAS", permission = "READ")
    public ResponseEntity<List<PersonJuridicResponseDTO>> getAllPersonasJuridicas() { 
        return ResponseEntity.ok(personaJuridicaService.findAll());
    }

    @GetMapping("/ruc")
    @RequirePermission(module = "PERSONAS", permission = "READ")
    public ResponseEntity<List<PersonJuridicResponseDTO>> getPersonasJuridicasByRUC(@RequestParam String ruc) { 
        return ResponseEntity.ok(personaJuridicaService.findByRuc(ruc.trim()));
    }

    @GetMapping("/razSocial")
    @RequirePermission(module = "PERSONAS", permission = "READ")
    public ResponseEntity<List<PersonJuridicResponseDTO>> getPersonasJuridicasByRazSocial(@RequestParam String razonSocial) { 
        return ResponseEntity.ok(personaJuridicaService.findByRazonSocial(razonSocial.trim()));
    }

    @GetMapping("/{id}")
    @RequirePermission(module = "PERSONAS", permission = "READ")
    public ResponseEntity<PersonJuridicResponseDTO> getPersonaJuridicaById(@PathVariable Integer id) { 
        return ResponseEntity.ok(personaJuridicaService.findById(id));
    }

    @PostMapping
    @RequirePermission(module = "PERSONAS", permission = "CREATE")
    public ResponseEntity<PersonJuridicResponseDTO> createPersonaJuridica(@Valid @RequestBody PersonJuridicRequestDTO personaJuridicaRequestDTO) { 
        return ResponseEntity.status(HttpStatus.CREATED).body(personaJuridicaService.save(personaJuridicaRequestDTO));
    }

    @PatchMapping("/{id}")
    @RequirePermission(module = "PERSONAS", permission = "UPDATE")
    public ResponseEntity<PersonJuridicResponseDTO> patchPersonaJuridica(@PathVariable Integer id, @Valid @RequestBody PersonJuridicRequestDTO personaJuridicaRequestDTO) {
        return ResponseEntity.ok(personaJuridicaService.patch(id, personaJuridicaRequestDTO));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(module = "PERSONAS", permission = "DELETE")
    public ResponseEntity<Void> deletePersonaJuridica(@PathVariable Integer id) {
        personaJuridicaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
