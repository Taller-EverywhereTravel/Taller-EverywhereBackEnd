package com.everywhere.backend.api;

import com.everywhere.backend.model.dto.SupplierRequestDTO;
import com.everywhere.backend.model.dto.SupplierResponseDTO;
import com.everywhere.backend.security.RequirePermission;
import com.everywhere.backend.service.ProveedorService; 
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supplier")
@RequiredArgsConstructor
public class ProveedorController {

    private final ProveedorService proveedorService;

    @GetMapping
    @RequirePermission(module = "PROVEEDORES", permission = "READ")
    public ResponseEntity<List<SupplierResponseDTO>> findAll() { 
        return ResponseEntity.ok(proveedorService.getAll());
    }

    @GetMapping("/{id}")
    @RequirePermission(module = "PROVEEDORES", permission = "READ")
    public ResponseEntity<SupplierResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(proveedorService.getById(id));
    }

    @PostMapping
    @RequirePermission(module = "PROVEEDORES", permission = "CREATE")
    public ResponseEntity<SupplierResponseDTO> create(@RequestBody  SupplierRequestDTO proveedorRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(proveedorService.create(proveedorRequestDTO));
    }

    @PatchMapping("/{id}")
    @RequirePermission(module = "PROVEEDORES", permission = "UPDATE")
    public ResponseEntity<SupplierResponseDTO> update(
            @PathVariable Integer id,
            @RequestBody SupplierRequestDTO proveedorRequestDTO) { 
        return ResponseEntity.ok(proveedorService.update(id, proveedorRequestDTO));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(module = "PROVEEDORES", permission = "DELETE")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        proveedorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
