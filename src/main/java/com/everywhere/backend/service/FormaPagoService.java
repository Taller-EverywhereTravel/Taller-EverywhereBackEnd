package com.everywhere.backend.service;

import com.everywhere.backend.model.dto.MethodPaymentRequestDTO;
import com.everywhere.backend.model.dto.MethodPaymentResponseDTO;

import java.util.List;

public interface FormaPagoService {
    List<MethodPaymentResponseDTO> findAll();
    MethodPaymentResponseDTO findById(Integer id);
    MethodPaymentResponseDTO findByCodigo(Integer codigo);
    List<MethodPaymentResponseDTO> findByDescripcion(String descripcion);
    MethodPaymentResponseDTO save(MethodPaymentRequestDTO formaPagoRequestDTO);
    MethodPaymentResponseDTO update(Integer id, MethodPaymentRequestDTO formaPagoRequestDTO);
    void deleteById(Integer id);
}
