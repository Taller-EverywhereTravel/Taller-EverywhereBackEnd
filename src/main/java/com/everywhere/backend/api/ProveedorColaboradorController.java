package com.everywhere.backend.api;

import com.everywhere.backend.model.dto.SupplierCollaboratorRequestDTO;
import com.everywhere.backend.model.dto.SupplierCollaboratorResponseDTO;
import com.everywhere.backend.security.RequirePermission;
import com.everywhere.backend.service.ProveedorColaboradorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supplier-collaborator")
@RequiredArgsConstructor
public class ProveedorColaboradorController {

    private final ProveedorColaboradorService service;

    @GetMapping
    @RequirePermission(module = "PROVEEDORES", permission = "READ")
    public ResponseEntity<List<SupplierCollaboratorResponseDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @RequirePermission(module = "PROVEEDORES", permission = "READ")
    public ResponseEntity<SupplierCollaboratorResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/supplier/{proveedorId}")
    @RequirePermission(module = "PROVEEDORES", permission = "READ")
    public ResponseEntity<List<SupplierCollaboratorResponseDTO>> getByProveedorId(@PathVariable Integer proveedorId) {
        return ResponseEntity.ok(service.findByProveedorId(proveedorId));
    }

    @PostMapping
    @RequirePermission(module = "PROVEEDORES", permission = "CREATE")
    public ResponseEntity<SupplierCollaboratorResponseDTO> create(
            @Valid @RequestBody SupplierCollaboratorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @PatchMapping("/{id}")
    @RequirePermission(module = "PROVEEDORES", permission = "UPDATE")
    public ResponseEntity<SupplierCollaboratorResponseDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody SupplierCollaboratorRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(module = "PROVEEDORES", permission = "DELETE")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
