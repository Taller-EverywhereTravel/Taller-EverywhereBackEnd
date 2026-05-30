package com.everywhere.backend.api;

import com.everywhere.backend.model.dto.MethodPaymentRequestDTO;
import com.everywhere.backend.model.dto.MethodPaymentResponseDTO;
import com.everywhere.backend.security.RequirePermission;
import com.everywhere.backend.service.FormaPagoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/method-payment")
@AllArgsConstructor
public class FormaPagoController {

    @Autowired
    private FormaPagoService formaPagoService;

    @GetMapping
    @RequirePermission(module = "FORMA-PAGO", permission = "READ")
    public ResponseEntity<List<MethodPaymentResponseDTO>> getAllFormasPago() { 
        return ResponseEntity.ok(formaPagoService.findAll());
    }

    @GetMapping("/{id}")
    @RequirePermission(module = "FORMA-PAGO", permission = "READ")
    public ResponseEntity<MethodPaymentResponseDTO> getFormaPagoById(@PathVariable Integer id) { 
        return ResponseEntity.ok(formaPagoService.findById(id));
    }

    @GetMapping("/code/{codigo}")
    @RequirePermission(module = "FORMA-PAGO", permission = "READ")
    public ResponseEntity<MethodPaymentResponseDTO> getFormaPagoByCodigo(@PathVariable Integer codigo) { 
        return ResponseEntity.ok(formaPagoService.findByCodigo(codigo));
    }

    @GetMapping("/description")
    @RequirePermission(module = "FORMA-PAGO", permission = "READ")
    public ResponseEntity<List<MethodPaymentResponseDTO>> getFormasPagoByDescripcion(@RequestParam String descripcion) { 
        return ResponseEntity.ok(formaPagoService.findByDescripcion(descripcion));
    }

    @PostMapping
    @RequirePermission(module = "FORMA-PAGO", permission = "CREATE")
    public ResponseEntity<MethodPaymentResponseDTO> createFormaPago(@Valid @RequestBody MethodPaymentRequestDTO formaPagoRequestDTO) { 
        return ResponseEntity.status(HttpStatus.CREATED).body(formaPagoService.save(formaPagoRequestDTO));
    }

    @PatchMapping("/{id}")
    @RequirePermission(module = "FORMA-PAGO", permission = "UPDATE")
    public ResponseEntity<MethodPaymentResponseDTO> updateFormaPago(@PathVariable Integer id, @Valid @RequestBody MethodPaymentRequestDTO formaPagoRequestDTO) { 
        return ResponseEntity.ok(formaPagoService.update(id, formaPagoRequestDTO));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(module = "FORMA-PAGO", permission = "DELETE")
    public ResponseEntity<Void> deleteFormaPago(@PathVariable Integer id) {
        formaPagoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
