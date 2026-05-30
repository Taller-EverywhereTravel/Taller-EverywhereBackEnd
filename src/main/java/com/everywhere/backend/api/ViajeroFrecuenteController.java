package com.everywhere.backend.api;

import com.everywhere.backend.model.dto.TravelerFrequentRequestDto;
import com.everywhere.backend.model.dto.TravelerFrequentResponseDto;
import com.everywhere.backend.security.RequirePermission;
import com.everywhere.backend.service.ViajeroFrecuenteService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/traveler-frequent")
public class ViajeroFrecuenteController {

    private final ViajeroFrecuenteService viajeroFrecuenteService;

    @PostMapping("/{viajeroId}")
    @RequirePermission(module = "VIAJEROS", permission = "CREATE")
    public ResponseEntity<TravelerFrequentResponseDto> crear(
            @PathVariable Integer viajeroId,
            @RequestBody TravelerFrequentRequestDto viajeroFrecuenteRequestDto) {
        return ResponseEntity.ok(viajeroFrecuenteService.crear(viajeroId, viajeroFrecuenteRequestDto));
    }

    @GetMapping
    @RequirePermission(module = "VIAJEROS", permission = "READ")
    public ResponseEntity<List<TravelerFrequentResponseDto>> findAll() {
        return ResponseEntity.ok(viajeroFrecuenteService.findAll());
    }
    
    @GetMapping("/{id}")
    @RequirePermission(module = "VIAJEROS", permission = "READ")
    public ResponseEntity<TravelerFrequentResponseDto> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(viajeroFrecuenteService.buscarPorId(id));
    }

    @GetMapping("/traveler/{viajeroId}")
    @RequirePermission(module = "VIAJEROS", permission = "READ")
    public ResponseEntity<List<TravelerFrequentResponseDto>> listarPorViajero(@PathVariable Integer viajeroId) {
        return ResponseEntity.ok(viajeroFrecuenteService.listarPorViajero(viajeroId));
    }

    @PatchMapping("/{id}")
    @RequirePermission(module = "VIAJEROS", permission = "UPDATE")
    public ResponseEntity<TravelerFrequentResponseDto> actualizar(
            @PathVariable Integer id,
            @RequestBody TravelerFrequentRequestDto viajeroFrecuenteRequestDto) {
        return ResponseEntity.ok(viajeroFrecuenteService.actualizar(id, viajeroFrecuenteRequestDto));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(module = "VIAJEROS", permission = "DELETE")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        viajeroFrecuenteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/{viajeroId}")
    @RequirePermission(module = "VIAJEROS", permission = "READ")
    public ResponseEntity<List<TravelerFrequentResponseDto>> buscarPorViajeroId(
            @PathVariable Integer viajeroId) {
        return ResponseEntity.ok(viajeroFrecuenteService.buscarPorViajeroId(viajeroId));
    }
}