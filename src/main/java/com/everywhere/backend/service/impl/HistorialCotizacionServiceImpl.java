package com.everywhere.backend.service.impl;

import com.everywhere.backend.exceptions.BadRequestException;
import com.everywhere.backend.exceptions.ResourceNotFoundException;
import com.everywhere.backend.mapper.HistorialCotizacionMapper;
import com.everywhere.backend.model.dto.HistorialCotizacionRequestDTO;
import com.everywhere.backend.model.dto.HistorialCotizacionResponseDTO;
import com.everywhere.backend.model.dto.HistorialCotizacionSimpleDTO;
import com.everywhere.backend.model.entity.Quotation;
import com.everywhere.backend.model.entity.StatusQuotation;
import com.everywhere.backend.model.entity.RecordQuotation;
import com.everywhere.backend.model.entity.User;
import com.everywhere.backend.repository.CotizacionRepository;
import com.everywhere.backend.repository.EstadoCotizacionRepository;
import com.everywhere.backend.repository.HistorialCotizacionRepository;
import com.everywhere.backend.repository.UserRepository;
import com.everywhere.backend.security.UserPrincipal;
import com.everywhere.backend.service.HistorialCotizacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistorialCotizacionServiceImpl implements HistorialCotizacionService {

    private final HistorialCotizacionRepository historialCotizacionRepository;
    private final HistorialCotizacionMapper historialCotizacionMapper;
    private final CotizacionRepository cotizacionRepository;
    private final EstadoCotizacionRepository estadoCotizacionRepository;
    private final UserRepository userRepository;

    @Override
    public List<HistorialCotizacionResponseDTO> findAll() {
        return mapToResponseList(historialCotizacionRepository.findAllWithRelations());
    }

    @Override
    public HistorialCotizacionResponseDTO findById(Integer id) {
        RecordQuotation historialCotizacion = historialCotizacionRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Historial de cotización no encontrado con ID: " + id));
        return historialCotizacionMapper.toResponseDTO(historialCotizacion);
    }

    @Override
    public List<HistorialCotizacionSimpleDTO> findByCotizacionId(Integer cotizacionId) {
        if (!cotizacionRepository.existsById(cotizacionId)) {
            throw new ResourceNotFoundException("Cotización no encontrada con ID: " + cotizacionId);
        }
        return mapToSimpleList(historialCotizacionRepository.findByCotizacionIdWithRelations(cotizacionId));
    }

    @Override
    public HistorialCotizacionResponseDTO save(HistorialCotizacionRequestDTO historialCotizacionRequestDTO) {
        if (historialCotizacionRequestDTO.getQuotationId() == null) {
            throw new BadRequestException("El campo cotizacionId es obligatorio para registrar historial");
        }

        if (historialCotizacionRequestDTO.getStatusQuotationId() == null) {
            throw new BadRequestException("El campo estadoCotizacionId es obligatorio para registrar historial");
        }

        RecordQuotation historialCotizacion = historialCotizacionMapper.toEntity(historialCotizacionRequestDTO);

        historialCotizacion.setQuotation(resolveCotizacion(historialCotizacionRequestDTO.getQuotationId()));
        historialCotizacion.setStatusQuotation(resolveEstadoCotizacion(historialCotizacionRequestDTO.getStatusQuotationId()));
        historialCotizacion.setUser(resolveUsuario(historialCotizacionRequestDTO.getUserId()));

        RecordQuotation saved = historialCotizacionRepository.save(historialCotizacion);
        return historialCotizacionMapper.toResponseDTO(saved);
    }

    @Override
    public HistorialCotizacionResponseDTO update(Integer id, HistorialCotizacionRequestDTO historialCotizacionRequestDTO) {
        if (!historialCotizacionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Historial de cotización no encontrado con ID: " + id);
        }

        RecordQuotation historialCotizacion = historialCotizacionRepository.findById(id).get();
        historialCotizacionMapper.updateEntityFromDTO(historialCotizacionRequestDTO, historialCotizacion);

        if (historialCotizacionRequestDTO.getQuotationId() != null) {
            historialCotizacion.setQuotation(resolveCotizacion(historialCotizacionRequestDTO.getQuotationId()));
        }

        if (historialCotizacionRequestDTO.getStatusQuotationId() != null) {
            historialCotizacion.setStatusQuotation(resolveEstadoCotizacion(historialCotizacionRequestDTO.getStatusQuotationId()));
        }

        if (historialCotizacionRequestDTO.getUserId() != null) {
            historialCotizacion.setUser(resolveUsuario(historialCotizacionRequestDTO.getUserId()));
        }

        RecordQuotation updated = historialCotizacionRepository.save(historialCotizacion);
        return historialCotizacionMapper.toResponseDTO(updated);
    }

    @Override
    public void deleteById(Integer id) {
        if (!historialCotizacionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Historial de cotización no encontrado con ID: " + id);
        }
        historialCotizacionRepository.deleteById(id);
    }

    @Override
    public HistorialCotizacionResponseDTO registrarCambioEstado(Integer cotizacionId,
                                                                Integer estadoCotizacionId,
                                                                String observacion) {
        HistorialCotizacionRequestDTO historialCotizacionRequestDTO = new HistorialCotizacionRequestDTO();
        historialCotizacionRequestDTO.setQuotationId(cotizacionId);
        historialCotizacionRequestDTO.setStatusQuotationId(estadoCotizacionId);
        historialCotizacionRequestDTO.setObservation(observacion);
        return save(historialCotizacionRequestDTO);
    }

    private Quotation resolveCotizacion(Integer cotizacionId) {
        return cotizacionRepository.findById(cotizacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Cotización no encontrada con ID: " + cotizacionId));
    }

    private StatusQuotation resolveEstadoCotizacion(Integer estadoCotizacionId) {
        return estadoCotizacionRepository.findById(estadoCotizacionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Estado de cotización no encontrado con ID: " + estadoCotizacionId));
    }

    private User resolveUsuario(Integer usuarioId) {
        if (usuarioId != null) {
            return userRepository.findById(usuarioId)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + usuarioId));
        }

        Integer authenticatedUserId = getAuthenticatedUserId();
        if (authenticatedUserId == null) {
            throw new BadRequestException("No se pudo identificar al usuario autenticado para registrar el historial");
        }

        return userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario autenticado no encontrado con ID: " + authenticatedUserId));
    }

    private Integer getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserPrincipal userPrincipal)) {
            return null;
        }

        if (userPrincipal.getUser() != null && userPrincipal.getUser().getId() != null) {
            return userPrincipal.getUser().getId();
        }

        return userPrincipal.getId();
    }

    private List<HistorialCotizacionResponseDTO> mapToResponseList(List<RecordQuotation> historialCotizaciones) {
        return historialCotizaciones.stream().map(historialCotizacionMapper::toResponseDTO).toList();
    }

    private List<HistorialCotizacionSimpleDTO> mapToSimpleList(List<RecordQuotation> historialCotizaciones) {
        return historialCotizaciones.stream().map(historialCotizacionMapper::toSimpleDTO).toList();
    }
}