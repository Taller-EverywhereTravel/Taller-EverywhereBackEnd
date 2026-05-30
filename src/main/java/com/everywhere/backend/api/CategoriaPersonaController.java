package com.everywhere.backend.api;

import com.everywhere.backend.model.dto.CategoryPersonaRequestDTO;
import com.everywhere.backend.model.dto.CategoryPersonaResponseDTO;
import com.everywhere.backend.model.dto.PersonNaturalResponseDTO;
import com.everywhere.backend.model.dto.PersonNaturalCategoryDTO;
import com.everywhere.backend.security.RequirePermission;
import com.everywhere.backend.service.CategoriaPersonaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category-person")
@RequiredArgsConstructor
public class CategoriaPersonaController {
    
    private final CategoriaPersonaService categoriaPersonaService;

    @GetMapping
    @RequirePermission(module = "CATEGORIA_PERSONAS", permission = "READ")
    public ResponseEntity<List<CategoryPersonaResponseDTO>> getAllCategorias() { 
        return ResponseEntity.ok(categoriaPersonaService.findAll());
    }

    @GetMapping("/name")
    @RequirePermission(module = "CATEGORIA_PERSONAS", permission = "READ")
    public ResponseEntity<List<CategoryPersonaResponseDTO>> getCategoriasByNombre(@RequestParam String nombre) { 
        return ResponseEntity.ok(categoriaPersonaService.findByNombre(nombre.trim()));
    }

    @GetMapping("/{id}")
    @RequirePermission(module = "CATEGORIA_PERSONAS", permission = "READ")
    public ResponseEntity<CategoryPersonaResponseDTO> getCategoriaById(@PathVariable Integer id) { 
        return ResponseEntity.ok(categoriaPersonaService.findById(id));
    }

    @PostMapping
    @RequirePermission(module = "CATEGORIA_PERSONAS", permission = "CREATE")
    public ResponseEntity<CategoryPersonaResponseDTO> createCategoria(@Valid @RequestBody CategoryPersonaRequestDTO categoriaPersonaRequestDTO) { 
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaPersonaService.save(categoriaPersonaRequestDTO));
    }

    @PatchMapping("/{id}")
    @RequirePermission(module = "CATEGORIA_PERSONAS", permission = "UPDATE")
    public ResponseEntity<CategoryPersonaResponseDTO> patchCategoria(@PathVariable Integer id, @RequestBody CategoryPersonaRequestDTO categoriaPersonaRequestDTO) { 
        return ResponseEntity.ok(categoriaPersonaService.patch(id, categoriaPersonaRequestDTO));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(module = "CATEGORIA_PERSONAS", permission = "DELETE")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Integer id) {
        categoriaPersonaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/person-natural/{personaNaturalId}/assign")
    @RequirePermission(module = "CATEGORIA_PERSONAS", permission = "UPDATE")
    public ResponseEntity<?> asignarCategoria(@PathVariable Integer personaNaturalId, @RequestBody PersonNaturalCategoryDTO categoriaDTO) { 
        return ResponseEntity.ok(categoriaPersonaService.asignarCategoria(personaNaturalId, categoriaDTO.getCategoryId()));
    }

    @PatchMapping("/person-natural/{personaNaturalId}/unassign")
    @RequirePermission(module = "CATEGORIA_PERSONAS", permission = "UPDATE")
    public ResponseEntity<?> desasignarCategoria(@PathVariable Integer personaNaturalId) { 
        return ResponseEntity.ok(categoriaPersonaService.desasignarCategoria(personaNaturalId));
    }

    @GetMapping("/category/{categoriaId}")
    @RequirePermission(module = "CATEGORIA_PERSONAS", permission = "READ")
    public ResponseEntity<List<PersonNaturalResponseDTO>> getPersonasPorCategoria(@PathVariable Integer categoriaId) { 
        return ResponseEntity.ok(categoriaPersonaService.findPersonasPorCategoria(categoriaId));
    }

    @GetMapping("/person-natural/{personaNaturalId}/category")
    @RequirePermission(module = "CATEGORIA_PERSONAS", permission = "READ")
    public ResponseEntity<CategoryPersonaResponseDTO> getCategoriaDePersona(@PathVariable Integer personaNaturalId) { 
        return ResponseEntity.ok(categoriaPersonaService.getCategoriaDePersona(personaNaturalId));
    }
}
