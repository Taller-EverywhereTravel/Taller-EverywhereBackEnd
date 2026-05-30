package com.everywhere.backend.api;

import com.everywhere.backend.model.dto.PersonNaturalRequestDTO;
import com.everywhere.backend.model.dto.PersonNaturalResponseDTO;
import com.everywhere.backend.model.dto.PersonNaturalViajeroDTO; 
import com.everywhere.backend.security.RequirePermission;
import com.everywhere.backend.service.PersonaNaturalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person-natural")
@RequiredArgsConstructor
public class PersonaNaturalController {

    private final PersonaNaturalService personaNaturalService;

    @GetMapping
    @RequirePermission(module = "PERSONAS", permission = "READ")
    public ResponseEntity<List<PersonNaturalResponseDTO>> getAllPersonasNaturales() { 
        return ResponseEntity.ok(personaNaturalService.findAll());
    }

    @GetMapping("/document")
    @RequirePermission(module = "PERSONAS", permission = "READ")
    public ResponseEntity<List<PersonNaturalResponseDTO>> getPersonasNaturalesByDocumento(@RequestParam String documento) { 
        return ResponseEntity.ok(personaNaturalService.findByDocumento(documento.trim()));
    }

    @GetMapping("/name")
    @RequirePermission(module = "PERSONAS", permission = "READ")
    public ResponseEntity<List<PersonNaturalResponseDTO>> getPersonasNaturalesByNombre(@RequestParam String nombres) { 
        return ResponseEntity.ok(personaNaturalService.findByNombres(nombres.trim()));
    }

    @GetMapping("/surname-paternal")
    @RequirePermission(module = "PERSONAS", permission = "READ")
    public ResponseEntity<List<PersonNaturalResponseDTO>> getPersonasNaturalesByApellidoPaterno(@RequestParam String apellidos) { 
        return ResponseEntity.ok(personaNaturalService.findByApellidosPaternos(apellidos.trim()));
    } 

    @GetMapping("/surname-maternal")
    @RequirePermission(module = "PERSONAS", permission = "READ")
    public ResponseEntity<List<PersonNaturalResponseDTO>> getPersonasNaturalesByApellidoMaterno(@RequestParam String apellidos) { 
        return ResponseEntity.ok(personaNaturalService.findByApellidosMaternos(apellidos.trim()));
    } 

    @GetMapping("/{id}")
    @RequirePermission(module = "PERSONAS", permission = "READ")
    public ResponseEntity<PersonNaturalResponseDTO> getPersonaNaturalById(@PathVariable Integer id) { 
        return ResponseEntity.ok(personaNaturalService.findById(id));
    }

    @PostMapping
    @RequirePermission(module = "PERSONAS", permission = "CREATE")
    public ResponseEntity<PersonNaturalResponseDTO> createPersonaNatural(@Valid @RequestBody PersonNaturalRequestDTO personaNaturalRequestDTO) { 
        return ResponseEntity.status(HttpStatus.CREATED).body(personaNaturalService.save(personaNaturalRequestDTO));
    }

    @PatchMapping("/{id}")
    @RequirePermission(module = "PERSONAS", permission = "UPDATE")
    public ResponseEntity<PersonNaturalResponseDTO> patchPersonaNatural(@PathVariable Integer id, @RequestBody PersonNaturalRequestDTO personaNaturalRequestDTO) { 
        return ResponseEntity.ok(personaNaturalService.patch(id, personaNaturalRequestDTO));
    }

    @PatchMapping("/{id}/associate-traveler")
    @RequirePermission(module = "PERSONAS", permission = "UPDATE")
    public ResponseEntity<PersonNaturalResponseDTO> patchAsociarViajero(@PathVariable Integer id, @RequestBody PersonNaturalViajeroDTO personaNaturalViajeroDTO) { 
        return ResponseEntity.ok(personaNaturalService.asociarViajero(id, personaNaturalViajeroDTO.getTravelerId()));
    }

    @PatchMapping("/{id}/dissociate-traveler")
    @RequirePermission(module = "PERSONAS", permission = "UPDATE")
    public ResponseEntity<PersonNaturalResponseDTO> desasociarViajero(@PathVariable Integer id) { 
        return ResponseEntity.ok(personaNaturalService.desasociarViajero(id));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(module = "PERSONAS", permission = "DELETE")
    public ResponseEntity<Void> deletePersonaNatural(@PathVariable Integer id) {
        personaNaturalService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}