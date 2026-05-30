package com.everywhere.backend.service.impl;

import com.everywhere.backend.exceptions.ResourceNotFoundException;
import com.everywhere.backend.mapper.PagoPaxMapper;
import com.everywhere.backend.model.dto.PaymentPaxRequestDTO;
import com.everywhere.backend.model.dto.PaymentPaxResponseDTO;
import com.everywhere.backend.model.entity.MethodPayment;
import com.everywhere.backend.model.entity.Liquidation;
import com.everywhere.backend.model.entity.PaymentPax;
import com.everywhere.backend.repository.FormaPagoRepository;
import com.everywhere.backend.repository.LiquidacionRepository;
import com.everywhere.backend.repository.PagoPaxRepository;
import com.everywhere.backend.service.PagoPaxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PagoPaxServiceImpl implements PagoPaxService {

    private final PagoPaxRepository pagoPaxRepository;
    private final LiquidacionRepository liquidacionRepository;
    private final FormaPagoRepository formaPagoRepository;
    private final PagoPaxMapper pagoPaxMapper;

    @Override
    @Transactional
    public PaymentPaxResponseDTO create(PaymentPaxRequestDTO requestDTO) {
        // Validar que existe la liquidación
        Liquidation liquidacion = liquidacionRepository.findById(requestDTO.getLiquidationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Liquidación no encontrada con ID: " + requestDTO.getLiquidationId()));

        // Validar que existe la forma de pago
        MethodPayment formaPago = formaPagoRepository.findById(requestDTO.getMethodPaymentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Forma de pago no encontrada con ID: " + requestDTO.getMethodPaymentId()));

        // Crear la entidad
        PaymentPax pagoPax = pagoPaxMapper.toEntity(requestDTO);
        pagoPax.setLiquidation(liquidacion);
        pagoPax.setMethodPayment(formaPago);

        // Guardar
        pagoPax = pagoPaxRepository.save(pagoPax);

        return pagoPaxMapper.toResponseDTO(pagoPax);
    }

    @Override
    public PaymentPaxResponseDTO findById(Integer id) {
        PaymentPax pagoPax = pagoPaxRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago Pax no encontrado con ID: " + id));

        return pagoPaxMapper.toResponseDTO(pagoPax);
    }

    @Override
    public List<PaymentPaxResponseDTO> findAll() {
        return pagoPaxRepository.findAllWithRelations().stream()
                .map(pagoPaxMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentPaxResponseDTO> findByLiquidacionId(Integer liquidacionId) {
        // Validar que existe la liquidación
        if (!liquidacionRepository.existsById(liquidacionId)) {
            throw new ResourceNotFoundException("Liquidación no encontrada con ID: " + liquidacionId);
        }

        return pagoPaxRepository.findByLiquidationId(liquidacionId).stream()
                .map(pagoPaxMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PaymentPaxResponseDTO update(Integer id, PaymentPaxRequestDTO requestDTO) {
        // Buscar el pago pax existente
        PaymentPax pagoPax = pagoPaxRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago Pax no encontrado con ID: " + id));

        // Actualizar datos básicos
        pagoPaxMapper.updateEntityFromRequestDTO(pagoPax, requestDTO);

        // Actualizar liquidación si cambió
        if (requestDTO.getLiquidationId() != null 
            && !requestDTO.getLiquidationId().equals(pagoPax.getLiquidation().getId())) {
            Liquidation liquidacion = liquidacionRepository.findById(requestDTO.getLiquidationId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Liquidación no encontrada con ID: " + requestDTO.getLiquidationId()));
            pagoPax.setLiquidation(liquidacion);
        }

        // Actualizar forma de pago si cambió
        if (requestDTO.getMethodPaymentId() != null 
            && !requestDTO.getMethodPaymentId().equals(pagoPax.getMethodPayment().getId())) {
            MethodPayment formaPago = formaPagoRepository.findById(requestDTO.getMethodPaymentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Forma de pago no encontrada con ID: " + requestDTO.getMethodPaymentId()));
            pagoPax.setMethodPayment(formaPago);
        }

        // Guardar
        pagoPax = pagoPaxRepository.save(pagoPax);

        return pagoPaxMapper.toResponseDTO(pagoPax);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!pagoPaxRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pago Pax no encontrado con ID: " + id);
        }

        pagoPaxRepository.deleteById(id);
    }
}
