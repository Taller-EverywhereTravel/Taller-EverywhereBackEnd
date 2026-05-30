package com.everywhere.backend.api;

import com.everywhere.backend.model.dto.ObservationLiquidationRequestDTO;
import com.everywhere.backend.model.dto.ObservationLiquidationResponseDTO;
import com.everywhere.backend.security.RequirePermission;
import com.everywhere.backend.service.ObservacionLiquidacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/observation-liquidation")
@RequiredArgsConstructor
public class ObservacionLiquidacionController {

    private final ObservacionLiquidacionService observacionLiquidacionService;

    @GetMapping
    @RequirePermission(module = "LIQUIDACIONES", permission = "READ")
    public ResponseEntity<List<ObservationLiquidationResponseDTO>> findAll() { 
        return ResponseEntity.ok(observacionLiquidacionService.findAll());
    }

    @GetMapping("/{id}")
    @RequirePermission(module = "LIQUIDACIONES", permission = "READ")
    public ResponseEntity<ObservationLiquidationResponseDTO> findById(@PathVariable Long id) { 
        return ResponseEntity.ok(observacionLiquidacionService.findById(id));
    }

    @PostMapping
    @RequirePermission(module = "LIQUIDACIONES", permission = "CREATE")
    public ResponseEntity<ObservationLiquidationResponseDTO> create(
            @RequestBody ObservationLiquidationRequestDTO observacionLiquidacionRequestDTO) { 
        return ResponseEntity.status(HttpStatus.CREATED).body(observacionLiquidacionService.save(observacionLiquidacionRequestDTO));
    }


    @PatchMapping("/{id}")
    @RequirePermission(module = "LIQUIDACIONES", permission = "UPDATE")
    public ResponseEntity<ObservationLiquidationResponseDTO> partialUpdate(
            @PathVariable Long id,
            @RequestBody ObservationLiquidationRequestDTO observacionLiquidacionRequestDTO) { 
        return ResponseEntity.ok(observacionLiquidacionService.update(id, observacionLiquidacionRequestDTO));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(module = "LIQUIDACIONES", permission = "DELETE")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        observacionLiquidacionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/liquidation/{liquidacionId}")
    @RequirePermission(module = "LIQUIDACIONES", permission = "READ")
    public ResponseEntity<List<ObservationLiquidationResponseDTO>> findByLiquidacionId(
            @PathVariable Integer liquidacionId) { 
        return ResponseEntity.ok(observacionLiquidacionService.findByLiquidacionId(liquidacionId));
    }
}
