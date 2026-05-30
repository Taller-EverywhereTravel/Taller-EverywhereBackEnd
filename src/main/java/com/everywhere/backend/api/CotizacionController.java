package com.everywhere.backend.api;

import com.everywhere.backend.model.dto.QuotationRequestDto;
import com.everywhere.backend.model.dto.QuotationResponseDto;
import com.everywhere.backend.model.dto.QuotationWithDetailResponseDTO;
import com.everywhere.backend.security.RequirePermission;
import com.everywhere.backend.service.CotizacionService;

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
@RequiredArgsConstructor
@RequestMapping("/quotation")
public class CotizacionController {

    private final CotizacionService cotizacionService;

    @PostMapping("/person/{personaId}")
    @RequirePermission(module = "COTIZACIONES", permission = "CREATE")
    public ResponseEntity<QuotationResponseDto> createWithPersona(
            @PathVariable Integer personaId, @RequestBody QuotationRequestDto cotizacionRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cotizacionService.create(cotizacionRequestDto, personaId));
    }

    @GetMapping("/{id}")
    @RequirePermission(module = "COTIZACIONES", permission = "READ")
    public ResponseEntity<QuotationResponseDto> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(cotizacionService.findById(id));
    }

    @GetMapping("/{id}/with-detail")
    @RequirePermission(module = "COTIZACIONES", permission = "READ")
    public ResponseEntity<QuotationWithDetailResponseDTO> getCotizacionConDetalles(@PathVariable Integer id) {
        return ResponseEntity.ok(cotizacionService.findByIdWithDetalles(id));
    }

    @GetMapping
    @RequirePermission(module = "COTIZACIONES", permission = "READ")
    public ResponseEntity<List<QuotationResponseDto>> findAll() {
        return ResponseEntity.ok(cotizacionService.findAll());
    }

    @PatchMapping("/{id}")
    @RequirePermission(module = "COTIZACIONES", permission = "UPDATE")
    public ResponseEntity<QuotationResponseDto> update(
            @PathVariable Integer id, @RequestBody QuotationRequestDto cotizacionRequestDto) {
        return ResponseEntity.ok(cotizacionService.update(id, cotizacionRequestDto));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(module = "COTIZACIONES", permission = "DELETE")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        cotizacionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/without-liquidation")
    @RequirePermission(module = "COTIZACIONES", permission = "READ")
    public ResponseEntity<List<QuotationResponseDto>> findCotizacionesSinLiquidacion() {
        return ResponseEntity.ok(cotizacionService.findCotizacionesSinLiquidacion());
    }

    /**
     * Generar documento DOCX de la cotización
     * Acepta GET (sin configuración) o POST (con configuración de vuelos)
     */
    @GetMapping("/{id}/generate-docx")
    @RequirePermission(module = "COTIZACIONES", permission = "READ")
    public ResponseEntity<Resource> generateDocx(@PathVariable Integer id) {
        ByteArrayInputStream docxStream = cotizacionService.generateDocx(id);

        InputStreamResource resource = new InputStreamResource(docxStream);

        String filename = "Cotizacion_" + id + ".docx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType
                        .parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                .body(resource);
    }

    // Endpoints para gestión de carpetas

    @GetMapping("/folder/{carpetaId}")
    @RequirePermission(module = "COTIZACIONES", permission = "READ")
    public ResponseEntity<List<QuotationResponseDto>> findByCarpeta(@PathVariable Integer carpetaId) {
        return ResponseEntity.ok(cotizacionService.findByCarpeta(carpetaId));
    }

    @GetMapping("/without-folder")
    @RequirePermission(module = "COTIZACIONES", permission = "READ")
    public ResponseEntity<List<QuotationResponseDto>> findSinCarpeta() {
        return ResponseEntity.ok(cotizacionService.findSinCarpeta());
    }

    @PatchMapping("/{id}/folder")
    @RequirePermission(module = "COTIZACIONES", permission = "UPDATE")
    public ResponseEntity<QuotationResponseDto> updateCarpeta(
            @PathVariable Integer id,
            @RequestParam(required = false) Integer carpetaId) {
        return ResponseEntity.ok(cotizacionService.updateCarpeta(id, carpetaId));
    }

}