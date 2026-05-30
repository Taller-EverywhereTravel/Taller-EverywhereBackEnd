package com.everywhere.backend.api;

import com.everywhere.backend.model.dto.LiquidationRequestDTO;
import com.everywhere.backend.model.dto.LiquidationResponseDTO;
import com.everywhere.backend.model.dto.LiquidationWithDetailResponseDTO;
import com.everywhere.backend.security.RequirePermission;
import com.everywhere.backend.service.LiquidacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/liquidation")
@RequiredArgsConstructor
public class LiquidacionController {

    private final LiquidacionService liquidacionService;

    @GetMapping
    @RequirePermission(module = "LIQUIDACIONES", permission = "READ")
    public ResponseEntity<List<LiquidationResponseDTO>> getAllLiquidaciones() {
        return ResponseEntity.ok(liquidacionService.findAll());
    }

    @GetMapping("/{id}")
    @RequirePermission(module = "LIQUIDACIONES", permission = "READ")
    public ResponseEntity<LiquidationResponseDTO> getLiquidacionById(@PathVariable Integer id) {
        return ResponseEntity.ok(liquidacionService.findById(id));
    }

    @PatchMapping("/{id}")
    @RequirePermission(module = "LIQUIDACIONES", permission = "UPDATE")
    public ResponseEntity<LiquidationResponseDTO> updateLiquidacion(
            @PathVariable Integer id, @RequestBody LiquidationRequestDTO liquidacionRequestDTO) {
        return ResponseEntity.ok(liquidacionService.update(id, liquidacionRequestDTO));
    }

    @GetMapping("/{id}/with-detail")
    @RequirePermission(module = "LIQUIDACIONES", permission = "READ")
    public ResponseEntity<LiquidationWithDetailResponseDTO> getLiquidacionConDetalles(@PathVariable Integer id) {
        return ResponseEntity.ok(liquidacionService.findByIdWithDetalles(id));
    }

    @GetMapping("/{id}/generate-excel")
    @RequirePermission(module = "LIQUIDACIONES", permission = "READ")
    public ResponseEntity<Resource> generateExcel(@PathVariable Integer id) {
        ByteArrayInputStream excelStream = liquidacionService.generateExcel(id);
        InputStreamResource resource = new InputStreamResource(excelStream);
        String filename = "Liquidacion_" + id + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);
    }

    @DeleteMapping("/{id}")
    @RequirePermission(module = "LIQUIDACIONES", permission = "DELETE")
    public ResponseEntity<Void> deleteLiquidacion(@PathVariable Integer id) {
        liquidacionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/quotation/{cotizacionId}")
    @RequirePermission(module = "LIQUIDACIONES", permission = "CREATE")
    public ResponseEntity<LiquidationResponseDTO> createLiquidacionConCotizacion(
            @PathVariable Integer cotizacionId, @Valid @RequestBody LiquidationRequestDTO liquidacionRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(liquidacionService.create(liquidacionRequestDTO, cotizacionId));
    }

    // Endpoints para gestión de carpetas

    @GetMapping("/folder/{carpetaId}")
    @RequirePermission(module = "LIQUIDACIONES", permission = "READ")
    public ResponseEntity<List<LiquidationResponseDTO>> findByCarpeta(@PathVariable Integer carpetaId) {
        return ResponseEntity.ok(liquidacionService.findByCarpeta(carpetaId));
    }

    @GetMapping("/without-folder")
    @RequirePermission(module = "LIQUIDACIONES", permission = "READ")
    public ResponseEntity<List<LiquidationResponseDTO>> findSinCarpeta() {
        return ResponseEntity.ok(liquidacionService.findSinCarpeta());
    }

    @PatchMapping("/{id}/folder")
    @RequirePermission(module = "LIQUIDACIONES", permission = "UPDATE")
    public ResponseEntity<LiquidationResponseDTO> updateCarpeta(
            @PathVariable Integer id,
            @RequestParam(required = false) Integer carpetaId) {
        return ResponseEntity.ok(liquidacionService.updateCarpeta(id, carpetaId));
    }
}
