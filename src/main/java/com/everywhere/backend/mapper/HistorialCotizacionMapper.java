package com.everywhere.backend.mapper;

import com.everywhere.backend.model.dto.HistorialCotizacionRequestDTO;
import com.everywhere.backend.model.dto.HistorialCotizacionResponseDTO;
import com.everywhere.backend.model.dto.HistorialCotizacionSimpleDTO;
import com.everywhere.backend.model.entity.RecordQuotation;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistorialCotizacionMapper {

    private final ModelMapper modelMapper;

    @PostConstruct
    public void configureMapping() {
        modelMapper.typeMap(HistorialCotizacionRequestDTO.class, RecordQuotation.class)
                .addMappings(mapper -> {
                    mapper.skip(RecordQuotation::setId);
                    mapper.skip(RecordQuotation::setUuid);
                    mapper.skip(RecordQuotation::setDateCreated);
                    mapper.skip(RecordQuotation::setUser);
                    mapper.skip(RecordQuotation::setQuotation);
                    mapper.skip(RecordQuotation::setStatusQuotation);
                });
    }

    public RecordQuotation toEntity(HistorialCotizacionRequestDTO historialCotizacionRequestDTO) {
        RecordQuotation historialCotizacion = new RecordQuotation();
        updateEntityFromDTO(historialCotizacionRequestDTO, historialCotizacion);
        return historialCotizacion;
    }

    public void updateEntityFromDTO(HistorialCotizacionRequestDTO historialCotizacionRequestDTO,
                                    RecordQuotation historialCotizacion) {
        if (historialCotizacionRequestDTO.getObservation() != null) {
            historialCotizacion.setObservation(historialCotizacionRequestDTO.getObservation());
        }
    }

    public HistorialCotizacionResponseDTO toResponseDTO(RecordQuotation historialCotizacion) {
        HistorialCotizacionResponseDTO historialCotizacionResponseDTO = new HistorialCotizacionResponseDTO();

        historialCotizacionResponseDTO.setId(historialCotizacion.getId());
        historialCotizacionResponseDTO.setUuid(historialCotizacion.getUuid());
        historialCotizacionResponseDTO.setObservation(historialCotizacion.getObservation());
        historialCotizacionResponseDTO.setDateCreated(historialCotizacion.getDateCreated());

        if (historialCotizacion.getUser() != null) {
            historialCotizacionResponseDTO.setUserId(historialCotizacion.getUser().getId());
            historialCotizacionResponseDTO.setUserName(historialCotizacion.getUser().getName());
            historialCotizacionResponseDTO.setUserMail(historialCotizacion.getUser().getMail());
        }

        if (historialCotizacion.getQuotation() != null) {
            historialCotizacionResponseDTO.setQuotationId(historialCotizacion.getQuotation().getId());
            historialCotizacionResponseDTO.setCodeQuotation(historialCotizacion.getQuotation().getCodeQuotation());
        }

        if (historialCotizacion.getStatusQuotation() != null) {
            historialCotizacionResponseDTO.setStatusQuotationId(historialCotizacion.getStatusQuotation().getId());
            historialCotizacionResponseDTO
                    .setStatusQuotationDescription(historialCotizacion.getStatusQuotation().getDescription());
        }

        return historialCotizacionResponseDTO;
    }

    public HistorialCotizacionSimpleDTO toSimpleDTO(RecordQuotation historialCotizacion) {
        HistorialCotizacionSimpleDTO historialCotizacionSimpleDTO = new HistorialCotizacionSimpleDTO();

        historialCotizacionSimpleDTO.setId(historialCotizacion.getId());
        historialCotizacionSimpleDTO.setUuid(historialCotizacion.getUuid());
        historialCotizacionSimpleDTO.setObservation(historialCotizacion.getObservation());
        historialCotizacionSimpleDTO.setDateCreated(historialCotizacion.getDateCreated());

        if (historialCotizacion.getUser() != null) {
            historialCotizacionSimpleDTO.setUserId(historialCotizacion.getUser().getId());
            historialCotizacionSimpleDTO.setUserName(historialCotizacion.getUser().getName());
            historialCotizacionSimpleDTO.setUserMail(historialCotizacion.getUser().getMail());
        }

        if (historialCotizacion.getStatusQuotation() != null) {
            historialCotizacionSimpleDTO.setStatusQuotationId(historialCotizacion.getStatusQuotation().getId());
            historialCotizacionSimpleDTO
                    .setStatusQuotationDescription(historialCotizacion.getStatusQuotation().getDescription());
        }

        return historialCotizacionSimpleDTO;
    }
}