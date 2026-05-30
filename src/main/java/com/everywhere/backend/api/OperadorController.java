package com.everywhere.backend.api;

import com.everywhere.backend.model.dto.OperatorRequestDTO;
import com.everywhere.backend.model.dto.OperatorResponseDTO;
import com.everywhere.backend.security.RequirePermission;
import com.everywhere.backend.service.OperadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/operator")
@RequiredArgsConstructor
public class OperadorController {

    private final OperadorService operadorService;

    @GetMapping
    @RequirePermission(module = "OPERADOR", permission = "READ")
    public ResponseEntity<List<OperatorResponseDTO>> findAll() { 
        return ResponseEntity.ok(operadorService.findAll());
    }

    @GetMapping("/{id}")
    @RequirePermission(module = "OPERADOR", permission = "READ")
    public ResponseEntity<OperatorResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(operadorService.findById(id));
    }

    @GetMapping("/name")
    public ResponseEntity<OperatorResponseDTO> getByNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(operadorService.findByNombre(nombre));
    }

    @PostMapping
    @RequirePermission(module = "OPERADOR", permission = "CREATE")
    public ResponseEntity<OperatorResponseDTO> create(@RequestBody OperatorRequestDTO operadorRequestDTO) { 
        return ResponseEntity.status(HttpStatus.CREATED).body(operadorService.save(operadorRequestDTO));
    }

    @PatchMapping("/{id}")
    @RequirePermission(module = "OPERADOR", permission = "UPDATE")
    public ResponseEntity<OperatorResponseDTO> partialUpdate(
            @PathVariable Integer id,
            @RequestBody OperatorRequestDTO dto) {
            return ResponseEntity.ok(operadorService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(module = "OPERADOR", permission = "DELETE")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        operadorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
