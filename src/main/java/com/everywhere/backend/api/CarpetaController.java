package com.everywhere.backend.api;

import com.everywhere.backend.exceptions.BadRequestException;
import com.everywhere.backend.model.dto.FolderRequestDto;
import com.everywhere.backend.model.dto.FolderResponseDto;
import com.everywhere.backend.security.RequirePermission;
import com.everywhere.backend.service.CarpetaService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; 

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/folder")
public class CarpetaController {

    private final CarpetaService carpetaService;

    @PostMapping
    @RequirePermission(module = "CARPETA", permission = "CREATE")
    public ResponseEntity<FolderResponseDto> create(
            @RequestBody FolderRequestDto carpetaRequestDto,
            @RequestParam(required = false) Integer carpetaPadreId,
            HttpServletRequest request) {
                
        if (!request.getParameterMap().isEmpty() && request.getParameter("carpetaPadreId") == null)
                throw new BadRequestException("El parámetro 'carpetaPadreId' está mal escrito o no es válido.");
        
        return ResponseEntity.status(HttpStatus.CREATED).body(carpetaService.create(carpetaRequestDto, carpetaPadreId));
    }

    @GetMapping("/{id}")
    @RequirePermission(module = "CARPETA", permission = "READ")
    public ResponseEntity<FolderResponseDto> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(carpetaService.findById(id));
    }

    @GetMapping
    @RequirePermission(module = "CARPETA", permission = "READ")
    public ResponseEntity<List<FolderResponseDto>> findAll() {
        return ResponseEntity.ok(carpetaService.findAll());
    }

    @PatchMapping("/{id}")
    @RequirePermission(module = "CARPETA", permission = "UPDATE")
    public ResponseEntity<FolderResponseDto> update(@PathVariable Integer id, @RequestBody FolderRequestDto carpetaRequestDto) {
        return ResponseEntity.ok(carpetaService.update(id, carpetaRequestDto));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(module = "CARPETA", permission = "DELETE")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        carpetaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/father/{carpetaPadreId}")
    @RequirePermission(module = "CARPETA", permission = "READ")
    public ResponseEntity<List<FolderResponseDto>> findByCarpetaPadre(@PathVariable Integer carpetaPadreId) {
        return ResponseEntity.ok(carpetaService.findByCarpetaPadreId(carpetaPadreId));
    }

    @GetMapping("/level/{nivel}")
    @RequirePermission(module = "CARPETA", permission = "READ")
    public ResponseEntity<List<FolderResponseDto>> findByNivel(@PathVariable Integer nivel) {
        return ResponseEntity.ok(carpetaService.findByNivel(nivel));
    }

    @GetMapping("/search")
    @RequirePermission(module = "CARPETA", permission = "READ")
    public ResponseEntity<List<FolderResponseDto>> findByNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(carpetaService.findByNombre(nombre));
    }

    @GetMapping("/date/{mes}")
    @RequirePermission(module = "CARPETA", permission = "READ")
    public ResponseEntity<List<FolderResponseDto>> findByMes(@PathVariable int mes) {
        return ResponseEntity.ok(carpetaService.findByMes(mes));
    }

    @GetMapping("/date")
    @RequirePermission(module = "CARPETA", permission = "READ")
    public ResponseEntity<List<FolderResponseDto>> findByRango(@RequestParam LocalDate inicio, @RequestParam LocalDate fin) {
        return ResponseEntity.ok(carpetaService.findByFechaCreacionBetween(inicio, fin));
    }

    @GetMapping("/recent")
    @RequirePermission(module = "CARPETA", permission = "READ")
    public ResponseEntity<List<FolderResponseDto>> findRecent(@RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(carpetaService.findRecent(limit));
    }

    @GetMapping("/roots") // Listar raíces (sin padre)
    @RequirePermission(module = "CARPETA", permission = "READ")
    public ResponseEntity<List<FolderResponseDto>> findRaices() {
        return ResponseEntity.ok(carpetaService.findRaices());
    }

    @GetMapping("/{id}/way")
    @RequirePermission(module = "CARPETA", permission = "READ")
    public ResponseEntity<List<FolderResponseDto>> findCamino(@PathVariable Integer id) {
        return ResponseEntity.ok(carpetaService.findCamino(id));
    }

    @GetMapping("/children/{id}")
    @RequirePermission(module = "CARPETA", permission = "READ")
    public ResponseEntity<List<FolderResponseDto>> findHijos(@PathVariable Integer id) {
        return ResponseEntity.ok(carpetaService.findByCarpetaPadreId(id));
    }

}