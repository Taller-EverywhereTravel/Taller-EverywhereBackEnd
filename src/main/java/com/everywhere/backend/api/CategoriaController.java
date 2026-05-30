package com.everywhere.backend.api;

import com.everywhere.backend.model.dto.CategoryRequestDto;
import com.everywhere.backend.model.dto.CategoryResponseDto;
import com.everywhere.backend.security.RequirePermission;
import com.everywhere.backend.service.CategoriaService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoriaController {

	private final CategoriaService categoriaService;

	@GetMapping
    @RequirePermission(module = "CATEGORIA", permission = "READ")
	public List<CategoryResponseDto> getAll() {
		return categoriaService.findAll();
	}

	@GetMapping("/{id}")
    @RequirePermission(module = "CATEGORIA", permission = "READ")
	public ResponseEntity<CategoryResponseDto> getById(@PathVariable int id) {
		return ResponseEntity.ok(categoriaService.findById(id));
	}

	@PostMapping
    @RequirePermission(module = "CATEGORIA", permission = "CREATE")
	public ResponseEntity<CategoryResponseDto> create(@RequestBody CategoryRequestDto categoriaRequestDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.create(categoriaRequestDto));
	}

	@PatchMapping("/{id}")
    @RequirePermission(module = "CATEGORIA", permission = "UPDATE")
	public ResponseEntity<CategoryResponseDto> patch(@PathVariable int id, @RequestBody CategoryRequestDto categoriaRequestDto) {
		return ResponseEntity.ok(categoriaService.patch(id, categoriaRequestDto));
	}

	@DeleteMapping("/{id}")
    @RequirePermission(module = "CATEGORIA", permission = "DELETE")
	public ResponseEntity<Void> delete(@PathVariable int id) {
		categoriaService.delete(id);
		return ResponseEntity.noContent().build();
	}
}