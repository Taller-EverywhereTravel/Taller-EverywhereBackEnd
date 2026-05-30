package com.everywhere.backend.api;

import com.everywhere.backend.model.dto.DetailQuotationRequestDto;
import com.everywhere.backend.model.dto.DetailQuotationResponseDto;
import com.everywhere.backend.security.RequirePermission;
import com.everywhere.backend.service.DetalleCotizacionService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/detail-quotation")
public class DetalleCotizacionController {

    private final DetalleCotizacionService detalleCotizacionService;

    @GetMapping
    @RequirePermission(module = "COTIZACIONES", permission = "READ")
    public ResponseEntity<List<DetailQuotationResponseDto>> getAll() {
        return ResponseEntity.ok(detalleCotizacionService.findAll());
    }

    @GetMapping("/{id}")
    @RequirePermission(module = "COTIZACIONES", permission = "READ")
    public ResponseEntity<DetailQuotationResponseDto> getById(@PathVariable int id) {
        return ResponseEntity.ok(detalleCotizacionService.findById(id));
    }

    @GetMapping("/quotation/{cotizacionId}")
    @RequirePermission(module = "COTIZACIONES", permission = "READ")
    public ResponseEntity<List<DetailQuotationResponseDto>> getByCotizacionId(@PathVariable int cotizacionId) {
        return ResponseEntity.ok(detalleCotizacionService.findByCotizacionId(cotizacionId));
    }

    @PostMapping("/quotation/{cotizacionId}")
    @RequirePermission(module = "COTIZACIONES", permission = "CREATE")
    public ResponseEntity<DetailQuotationResponseDto> create(
            @PathVariable int cotizacionId, @RequestBody DetailQuotationRequestDto detalleCotizacionRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(detalleCotizacionService.create(detalleCotizacionRequestDto, cotizacionId));
    }

    @PatchMapping("/{id}")
    @RequirePermission(module = "COTIZACIONES", permission = "UPDATE")
    public ResponseEntity<DetailQuotationResponseDto> patch(
            @PathVariable int id, @RequestBody DetailQuotationRequestDto detalleCotizacionRequestDto) {
        return ResponseEntity.ok(detalleCotizacionService.patch(id, detalleCotizacionRequestDto));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(module = "COTIZACIONES", permission = "DELETE")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        detalleCotizacionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}