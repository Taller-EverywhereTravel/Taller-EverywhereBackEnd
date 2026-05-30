package com.everywhere.backend.api;

import com.everywhere.backend.model.dto.TravelerWithPersonResponseDTO;
import com.everywhere.backend.model.dto.TravelerRequestDTO;
import com.everywhere.backend.model.dto.TravelerResponseDTO;
import com.everywhere.backend.security.RequirePermission;
import com.everywhere.backend.service.ViajeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/traveler")
@RequiredArgsConstructor
public class ViajeroController {

    private final ViajeroService viajeroService;

    @GetMapping
    @RequirePermission(module = "VIAJEROS", permission = "READ")
    public ResponseEntity<List<TravelerResponseDTO>> getAllViajeros() {
        return ResponseEntity.ok(viajeroService.findAll());
    }

    @GetMapping("/nationality")
    @RequirePermission(module = "VIAJEROS", permission = "READ")
    public ResponseEntity<List<TravelerResponseDTO>> getViajeroByNacionalidad(@RequestParam String nacionalidad) {
        return ResponseEntity.ok(viajeroService.findByNacionalidad(nacionalidad.trim()));
    }

    @GetMapping("/residence")
    @RequirePermission(module = "VIAJEROS", permission = "READ")
    public ResponseEntity<List<TravelerResponseDTO>> getViajeroByResidencia(@RequestParam String residencia) {
        return ResponseEntity.ok(viajeroService.findByResidencia(residencia.trim()));
    }

    @GetMapping("/{id}")
    @RequirePermission(module = "VIAJEROS", permission = "READ")
    public ResponseEntity<TravelerResponseDTO> getViajeroById(@PathVariable Integer id) {
        return ResponseEntity.ok(viajeroService.findById(id));
    }

    @PostMapping
    @RequirePermission(module = "VIAJEROS", permission = "CREATE")
    public ResponseEntity<TravelerResponseDTO> createViajero(@RequestBody TravelerRequestDTO viajeroRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(viajeroService.save(viajeroRequestDTO));
    }

    @PatchMapping("/{id}")
    @RequirePermission(module = "VIAJEROS", permission = "UPDATE")
    public ResponseEntity<TravelerResponseDTO> patch(@PathVariable Integer id, @RequestBody TravelerRequestDTO viajeroRequestDTO) { 
        return ResponseEntity.ok(viajeroService.patch(id, viajeroRequestDTO));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(module = "VIAJEROS", permission = "DELETE")
    public ResponseEntity<Void> deleteViajero(@PathVariable Integer id) {
        viajeroService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/with-natural-person")
    @RequirePermission(module = "VIAJEROS", permission = "READ")
    public ResponseEntity<List<TravelerWithPersonResponseDTO>> findAllWithPersonaNatural() {
        return ResponseEntity.ok(viajeroService.findAllWithPersonaNatural());
    }
}
