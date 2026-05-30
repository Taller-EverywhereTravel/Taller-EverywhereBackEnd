package com.everywhere.backend.api;

import com.everywhere.backend.model.dto.ReceiptResponseDTO;
import com.everywhere.backend.model.dto.ReceiptUpdateDTO;
import com.everywhere.backend.security.RequirePermission;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.everywhere.backend.service.ReciboService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/receipt")
public class ReciboController {

    private final ReciboService reciboService;

    @PostMapping
    @RequirePermission(module = "RECIBOS", permission = "CREATE")
    public ResponseEntity<ReceiptResponseDTO> createRecibo(
            @RequestParam Integer cotizacionId,
            @RequestParam(required = false) Integer personaJuridicaId,
            @RequestParam(required = false) Integer sucursalId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reciboService.createRecibo(cotizacionId, personaJuridicaId, sucursalId));
    }

    @GetMapping
    @RequirePermission(module = "RECIBOS", permission = "READ")
    public ResponseEntity<List<ReceiptResponseDTO>> getAllRecibos() {
        return ResponseEntity.ok(reciboService.findAll());
    }

    @GetMapping("/{id}")
    @RequirePermission(module = "RECIBOS", permission = "READ")
    public ResponseEntity<?> getReciboById(@PathVariable Integer id) {
        return ResponseEntity.ok(reciboService.findById(id));
    }

    @PatchMapping("/{id}")
    @RequirePermission(module = "RECIBOS", permission = "UPDATE")
    public ResponseEntity<?> updateRecibo(@PathVariable Integer id,
            @Valid @RequestBody ReceiptUpdateDTO reciboUpdateDTO) {
        return ResponseEntity.ok(reciboService.patchRecibo(id, reciboUpdateDTO));
    }

    // Endpoints para gestión de carpetas

    @GetMapping("/folder/{carpetaId}")
    @RequirePermission(module = "RECIBOS", permission = "READ")
    public ResponseEntity<List<ReceiptResponseDTO>> findByCarpeta(@PathVariable Integer carpetaId) {
        return ResponseEntity.ok(reciboService.findByCarpeta(carpetaId));
    }

    @GetMapping("/without-folder")
    @RequirePermission(module = "RECIBOS", permission = "READ")
    public ResponseEntity<List<ReceiptResponseDTO>> findSinCarpeta() {
        return ResponseEntity.ok(reciboService.findSinCarpeta());
    }

    @PatchMapping("/{id}/folder")
    @RequirePermission(module = "RECIBOS", permission = "UPDATE")
    public ResponseEntity<ReceiptResponseDTO> updateCarpeta(
            @PathVariable Integer id,
            @RequestParam(required = false) Integer carpetaId) {
        return ResponseEntity.ok(reciboService.updateCarpeta(id, carpetaId));
    }
}
