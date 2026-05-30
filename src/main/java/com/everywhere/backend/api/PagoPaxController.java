package com.everywhere.backend.api;

import com.everywhere.backend.model.dto.PaymentPaxRequestDTO;
import com.everywhere.backend.model.dto.PaymentPaxResponseDTO;
import com.everywhere.backend.security.RequirePermission;
import com.everywhere.backend.service.PagoPaxService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment-pax")
@RequiredArgsConstructor
public class PagoPaxController {

    private final PagoPaxService pagoPaxService;

    /**
     * Crear un nuevo pago pax
     */
    @PostMapping
    @RequirePermission(module = "PAGOS_PAX", permission = "CREATE")
    public ResponseEntity<PaymentPaxResponseDTO> createPagoPax(@Valid @RequestBody PaymentPaxRequestDTO requestDTO) {
        PaymentPaxResponseDTO responseDTO = pagoPaxService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /**
     * Obtener todos los pagos pax
     */
    @GetMapping
    @RequirePermission(module = "PAGOS_PAX", permission = "READ")
    public ResponseEntity<List<PaymentPaxResponseDTO>> getAllPagosPax() {
        return ResponseEntity.ok(pagoPaxService.findAll());
    }

    /**
     * Obtener un pago pax por ID
     */
    @GetMapping("/{id}")
    @RequirePermission(module = "PAGOS_PAX", permission = "READ")
    public ResponseEntity<PaymentPaxResponseDTO> getPagoPaxById(@PathVariable Integer id) {
        return ResponseEntity.ok(pagoPaxService.findById(id));
    }

    /**
     * Obtener todos los pagos pax de una liquidación
     */
    @GetMapping("/liquidation/{liquidacionId}")
    @RequirePermission(module = "PAGOS_PAX", permission = "READ")
    public ResponseEntity<List<PaymentPaxResponseDTO>> getPagosPaxByLiquidacion(@PathVariable Integer liquidacionId) {
        return ResponseEntity.ok(pagoPaxService.findByLiquidacionId(liquidacionId));
    }

    /**
     * Actualizar un pago pax existente
     */
    @PatchMapping("/{id}")
    @RequirePermission(module = "PAGOS_PAX", permission = "UPDATE")
    public ResponseEntity<PaymentPaxResponseDTO> updatePagoPax(
            @PathVariable Integer id,
            @Valid @RequestBody PaymentPaxRequestDTO requestDTO) {
        return ResponseEntity.ok(pagoPaxService.update(id, requestDTO));
    }

    /**
     * Eliminar un pago pax
     */
    @DeleteMapping("/{id}")
    @RequirePermission(module = "PAGOS_PAX", permission = "DELETE")
    public ResponseEntity<Void> deletePagoPax(@PathVariable Integer id) {
        pagoPaxService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
