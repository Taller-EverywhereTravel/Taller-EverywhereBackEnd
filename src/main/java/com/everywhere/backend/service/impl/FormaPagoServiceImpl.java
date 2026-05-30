package com.everywhere.backend.service.impl;

import com.everywhere.backend.exceptions.ConflictException;
import com.everywhere.backend.mapper.FormaPagoMapper;
import com.everywhere.backend.model.dto.MethodPaymentRequestDTO;
import com.everywhere.backend.model.dto.MethodPaymentResponseDTO;
import com.everywhere.backend.model.entity.MethodPayment;
import com.everywhere.backend.repository.CotizacionRepository;
import com.everywhere.backend.repository.FormaPagoRepository;
import com.everywhere.backend.service.FormaPagoService;

import lombok.RequiredArgsConstructor;

import com.everywhere.backend.exceptions.ResourceNotFoundException; 
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormaPagoServiceImpl implements FormaPagoService {

    private final FormaPagoRepository formaPagoRepository;
    private final FormaPagoMapper formaPagoMapper;
    private final CotizacionRepository cotizacionRepository;

    @Override
    public List<MethodPaymentResponseDTO> findAll() {
        return mapToResponseList(formaPagoRepository.findAll());
    }

    @Override
    public MethodPaymentResponseDTO findById(Integer id) {
        return formaPagoRepository.findById(id).map(formaPagoMapper::toResponseDTO)
            .orElseThrow(() -> new ResourceNotFoundException("Forma de pago no encontrada con ID: " + id));
    }

    @Override
    public MethodPaymentResponseDTO findByCodigo(Integer codigo) {
        return formaPagoRepository.findByCode(codigo).map(formaPagoMapper::toResponseDTO)
            .orElseThrow(() -> new ResourceNotFoundException("Forma de pago no encontrada con código: " + codigo));
    }

    @Override
    public List<MethodPaymentResponseDTO> findByDescripcion(String descripcion) {
        return mapToResponseList(formaPagoRepository.findByDescriptionContainingIgnoreCase(descripcion));
    }

    @Override
    public MethodPaymentResponseDTO save(MethodPaymentRequestDTO formaPagoRequestDTO) {
        MethodPayment formaPago = formaPagoMapper.toEntity(formaPagoRequestDTO);
        return formaPagoMapper.toResponseDTO(formaPagoRepository.save(formaPago));
    }

    @Override
    public MethodPaymentResponseDTO update(Integer id, MethodPaymentRequestDTO formaPagoRequestDTO) {
        if (!formaPagoRepository.existsById(id))
            throw new ResourceNotFoundException("Forma de pago no encontrada con ID: " + id);

        MethodPayment formaPago = formaPagoRepository.findById(id).get();

        if (formaPagoRequestDTO.getCode() != null && 
            !formaPagoRequestDTO.getCode().equals(formaPago.getCode()) &&
            formaPagoRepository.existsByCode(formaPagoRequestDTO.getCode())) {
            throw new DataIntegrityViolationException("Ya existe una forma de pago con el código: " + formaPagoRequestDTO.getCode());
        }

        if (formaPagoRequestDTO.getCode() != null)
            formaPago.setCode(formaPagoRequestDTO.getCode());

        if (formaPagoRequestDTO.getDescription() != null && !formaPagoRequestDTO.getDescription().trim().isEmpty())
            formaPago.setDescription(formaPagoRequestDTO.getDescription());

        return formaPagoMapper.toResponseDTO(formaPagoRepository.save(formaPago));
    }

    @Override
    public void deleteById(Integer id) {
        if (!formaPagoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Forma de pago no encontrada con ID: " + id);
        }

        long cotizacionesCount = cotizacionRepository.countByMethodPaymentId(id);
        if (cotizacionesCount > 0) {
            throw new ConflictException(
                    "No se puede eliminar esta forma de pago porque tiene " + cotizacionesCount + " cotización(es) asociada(s).",
                    "/api/v1/formas-pago/" + id
            );
        }

        formaPagoRepository.deleteById(id);
    }

    private List<MethodPaymentResponseDTO> mapToResponseList(List<MethodPayment> formasPago) {
        return formasPago.stream().map(formaPagoMapper::toResponseDTO).toList();
    }
}