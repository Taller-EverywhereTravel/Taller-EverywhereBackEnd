package com.everywhere.backend.api;

import com.everywhere.backend.model.dto.SupplierContactRequestDTO;
import com.everywhere.backend.model.dto.SupplierContactResponseDTO;
import com.everywhere.backend.security.RequirePermission;
import com.everywhere.backend.service.ProveedorContactoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supplier-contact")
@RequiredArgsConstructor
public class ProveedorContactoController {

    private final ProveedorContactoService service;

    @GetMapping
    @RequirePermission(module = "PROVEEDORES", permission = "READ")
    public ResponseEntity<List<SupplierContactResponseDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @RequirePermission(module = "PROVEEDORES", permission = "READ")
    public ResponseEntity<SupplierContactResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/supplier/{proveedorId}")
    @RequirePermission(module = "PROVEEDORES", permission = "READ")
    public ResponseEntity<List<SupplierContactResponseDTO>> getByProveedorId(@PathVariable Integer proveedorId) {
        return ResponseEntity.ok(service.findByProveedorId(proveedorId));
    }

    @GetMapping("/group/{grupoId}")
    @RequirePermission(module = "PROVEEDORES", permission = "READ")
    public ResponseEntity<List<SupplierContactResponseDTO>> getByGrupoContactoId(@PathVariable Integer grupoId) {
        return ResponseEntity.ok(service.findByGrupoContactoId(grupoId));
    }

    @PostMapping
    @RequirePermission(module = "PROVEEDORES", permission = "CREATE")
    public ResponseEntity<SupplierContactResponseDTO> create(@Valid @RequestBody SupplierContactRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @PatchMapping("/{id}")
    @RequirePermission(module = "PROVEEDORES", permission = "UPDATE")
    public ResponseEntity<SupplierContactResponseDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody SupplierContactRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(module = "PROVEEDORES", permission = "DELETE")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
