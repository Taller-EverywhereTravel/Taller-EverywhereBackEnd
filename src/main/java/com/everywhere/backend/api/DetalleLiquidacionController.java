package com.everywhere.backend.api;

import com.everywhere.backend.model.dto.DetailLiquidationRequestDTO;
import com.everywhere.backend.model.dto.DetailLiquidationResponseDTO;
import com.everywhere.backend.model.dto.DetailLiquidationWithoutLiquidationDTO;
import com.everywhere.backend.security.RequirePermission;
import com.everywhere.backend.service.DetalleLiquidacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detail-liquidation")
@RequiredArgsConstructor
public class DetalleLiquidacionController {

    private final DetalleLiquidacionService detalleLiquidacionService;

    @GetMapping
    @RequirePermission(module = "LIQUIDACIONES", permission = "READ")
    public ResponseEntity<List<DetailLiquidationResponseDTO>> getAllDetallesLiquidacion() { 
        return ResponseEntity.ok(detalleLiquidacionService.findAll());
    }

    @GetMapping("/{id}")
    @RequirePermission(module = "LIQUIDACIONES", permission = "READ")
    public ResponseEntity<DetailLiquidationResponseDTO> getDetalleLiquidacionById(@PathVariable Integer id) { 
        return ResponseEntity.ok(detalleLiquidacionService.findById(id));
    }

    @GetMapping("/liquidation/{liquidacionId}")
    @RequirePermission(module = "LIQUIDACIONES", permission = "READ")
    public ResponseEntity<List<DetailLiquidationWithoutLiquidationDTO>> getDetallesByLiquidacionId(@PathVariable Integer liquidacionId) { 
        return ResponseEntity.ok(detalleLiquidacionService.findByLiquidacionIdSinLiquidacion(liquidacionId));
    }

    @PostMapping
    @RequirePermission(module = "LIQUIDACIONES", permission = "CREATE")
    public ResponseEntity<DetailLiquidationResponseDTO> createDetalleLiquidacion(
            @Valid @RequestBody DetailLiquidationRequestDTO detalleLiquidacionRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(detalleLiquidacionService.save(detalleLiquidacionRequestDTO));
    }

    @PatchMapping("/{id}")
    @RequirePermission(module = "LIQUIDACIONES", permission = "UPDATE")
    public ResponseEntity<DetailLiquidationResponseDTO> updateDetalleLiquidacion(
            @PathVariable Integer id, @RequestBody DetailLiquidationRequestDTO detalleLiquidacionRequestDTO) {
        return ResponseEntity.ok(detalleLiquidacionService.update(id, detalleLiquidacionRequestDTO));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(module = "LIQUIDACIONES", permission = "DELETE")
    public ResponseEntity<Void> deleteDetalleLiquidacion(@PathVariable Integer id) {
        detalleLiquidacionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}