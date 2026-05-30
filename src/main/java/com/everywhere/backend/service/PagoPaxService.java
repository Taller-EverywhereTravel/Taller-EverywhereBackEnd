package com.everywhere.backend.service;

import com.everywhere.backend.model.dto.PaymentPaxRequestDTO;
import com.everywhere.backend.model.dto.PaymentPaxResponseDTO;

import java.util.List;

public interface PagoPaxService {

    /**
     * Crea un nuevo pago pax
     */
    PaymentPaxResponseDTO create(PaymentPaxRequestDTO requestDTO);

    /**
     * Obtiene un pago pax por ID
     */
    PaymentPaxResponseDTO findById(Integer id);

    /**
     * Obtiene todos los pagos pax
     */
    List<PaymentPaxResponseDTO> findAll();

    /**
     * Obtiene todos los pagos pax de una liquidación específica
     */
    List<PaymentPaxResponseDTO> findByLiquidacionId(Integer liquidacionId);

    /**
     * Actualiza un pago pax existente
     */
    PaymentPaxResponseDTO update(Integer id, PaymentPaxRequestDTO requestDTO);

    /**
     * Elimina un pago pax por ID
     */
    void delete(Integer id);
}
