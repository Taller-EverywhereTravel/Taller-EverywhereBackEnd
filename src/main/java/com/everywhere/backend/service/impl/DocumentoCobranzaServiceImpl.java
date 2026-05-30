package com.everywhere.backend.service.impl;

import com.everywhere.backend.model.dto.QuotationWithDetailResponseDTO;
import com.everywhere.backend.model.dto.DocumentCollectionResponseDTO;
import com.everywhere.backend.model.dto.DocumentCollectionUpdateDTO;
import com.everywhere.backend.exceptions.ResourceNotFoundException;
import com.everywhere.backend.mapper.DocumentoCobranzaMapper;
import com.everywhere.backend.model.entity.DocumentCollection;
import com.everywhere.backend.model.entity.MethodPayment;
import com.everywhere.backend.model.entity.PersonJuridic;
import com.everywhere.backend.repository.PersonaJuridicaRepository;
import com.everywhere.backend.repository.PersonaNaturalRepository;
import com.everywhere.backend.model.entity.PersonNatural;
import com.everywhere.backend.model.entity.Branch;
import com.everywhere.backend.repository.SucursalRepository;
import com.everywhere.backend.model.entity.DetailDocumentCollection;
import com.everywhere.backend.model.entity.DetailDocument;
import com.everywhere.backend.repository.DetalleDocumentoCobranzaRepository;
import com.everywhere.backend.repository.DetalleDocumentoRepository;
import com.everywhere.backend.repository.DocumentoCobranzaRepository;
import com.everywhere.backend.repository.FormaPagoRepository;
import com.everywhere.backend.repository.NaturalJuridicoRepository;
import com.everywhere.backend.model.entity.Folder;
import com.everywhere.backend.repository.CarpetaRepository;
import com.everywhere.backend.security.UserPrincipal;
import com.everywhere.backend.service.CotizacionService;
import com.everywhere.backend.service.DetalleCotizacionService;
import com.everywhere.backend.service.DocumentoCobranzaService;
import com.everywhere.backend.model.dto.DetailQuotationResponseDto;
import com.everywhere.backend.model.entity.Product;
import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DocumentoCobranzaServiceImpl implements DocumentoCobranzaService {

    private final CotizacionService cotizacionService;
    private final DocumentoCobranzaRepository documentoCobranzaRepository;
    private final DetalleDocumentoCobranzaRepository detalleDocumentoCobranzaRepository;
    private final DetalleDocumentoRepository detalleDocumentoRepository;
    private final DocumentoCobranzaMapper documentoCobranzaMapper;
    private final PersonaJuridicaRepository personaJuridicaRepository;
    private final SucursalRepository sucursalRepository;
    private final FormaPagoRepository formaPagoRepository;
    private final NaturalJuridicoRepository naturalJuridicoRepository;
    private final PersonaNaturalRepository personaNaturalRepository;
    private final DetalleCotizacionService detalleCotizacionService;
    private final CarpetaRepository carpetaRepository;
    private final com.everywhere.backend.util.pdf.DocumentoCobranzaPdfGenerator documentoCobranzaPdfGenerator;

    @Override
    @Transactional
    public DocumentCollectionResponseDTO createDocumentoCobranza(Integer cotizacionId, Integer personaJuridicaId,
            Integer sucursalId) {
        if (cotizacionId == null)
            throw new IllegalArgumentException("El ID de la cotización no puede ser nulo");

        if (documentoCobranzaRepository.findByCotizacionId(cotizacionId).isPresent())
            throw new DataIntegrityViolationException(
                    "Ya existe un documento de cobranza para la cotización ID: " + cotizacionId);

        QuotationWithDetailResponseDTO cotizacion = cotizacionService.findByIdWithDetalles(cotizacionId);

        String[] serieCorrelativo = generateNextDocumentNumber();
        System.out.println("=== GENERANDO DOCUMENTO ===");
        System.out.println("Serie: " + serieCorrelativo[0]);
        System.out.println("Correlativo: " + serieCorrelativo[1]);
        DocumentCollection documentoCobranza = documentoCobranzaMapper.fromCotizacion(cotizacion, serieCorrelativo[0],
                Integer.parseInt(serieCorrelativo[1]));

        // Validar y setear PersonaJuridica si fue proporcionada
        if (personaJuridicaId != null) {
            PersonJuridic personaJuridica = personaJuridicaRepository.findById(personaJuridicaId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Persona jurídica no encontrada con ID: " + personaJuridicaId));

            // Validar que la PersonaJuridica esté asociada a la PersonaNatural de la cotización
            if (cotizacion.getPerson() != null) {
                Integer personaId = cotizacion.getPerson().getId();
                PersonNatural personaNatural = personaNaturalRepository.findByPersonasId(personaId)
                        .orElse(null);

                if (personaNatural != null) {
                    // Verificar que existe la relación NaturalJuridico
                    boolean relacionExiste = naturalJuridicoRepository
                            .findByPersonaNaturalIdAndPersonaJuridicaId(personaNatural.getId(), personaJuridicaId)
                            .isPresent();

                    if (!relacionExiste) {
                        throw new IllegalArgumentException("La persona jurídica ID " + personaJuridicaId +
                                " no está asociada a la persona natural de la cotización");
                    }
                }
            }

            documentoCobranza.setPersonJuridic(personaJuridica);
        }

        // Validar y setear Sucursal si fue proporcionada
        if (sucursalId != null) {
            Branch sucursal = sucursalRepository.findById(sucursalId)
                    .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada con ID: " + sucursalId));
            documentoCobranza.setBranch(sucursal);
        }

        documentoCobranza = documentoCobranzaRepository.save(documentoCobranza);

        // Crear detalles desde cotización con repartición por cantidad
        crearDetallesDesdeCotizacion(documentoCobranza, cotizacionId);

        return documentoCobranzaMapper.toResponseDTO(documentoCobranza);
    }

    @Override
    public ByteArrayInputStream generatePdf(Long documentoId) {
        DocumentCollection documentoCobranza = documentoCobranzaRepository.findByIdWithRelations(documentoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Documento de cobranza no encontrado con ID: " + documentoId));

        documentoCobranzaRepository.findByIdWithDetalles(documentoId)
                .ifPresent(d -> documentoCobranza.setDetail(d.getDetail()));

        DocumentCollectionResponseDTO documentoCobranzaResponseDTO = documentoCobranzaMapper
                .toResponseDTO(documentoCobranza);
        String userName = getAuthenticatedUserName();
        return documentoCobranzaPdfGenerator.generatePdf(documentoCobranzaResponseDTO, userName);
    }

    @Override
    public DocumentCollectionResponseDTO findById(Long id) {
        DocumentCollection documentoCobranza = documentoCobranzaRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Documento de cobranza no encontrado con ID: " + id));
        return documentoCobranzaMapper.toResponseDTO(documentoCobranza);
    }

    @Override
    public DocumentCollectionResponseDTO findBySerieAndCorrelativo(String serie, Integer correlativo) {
        DocumentCollection documentoCobranza = documentoCobranzaRepository.findBySerieAndCorrelativo(serie, correlativo)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Documento de cobranza no encontrado con serie: " + serie + " y correlativo: " + correlativo));
        return documentoCobranzaMapper.toResponseDTO(documentoCobranza);
    }

    @Override
    public List<DocumentCollectionResponseDTO> findAll() {
        return mapToResponseList(documentoCobranzaRepository.findAllForListing());
    }

    @Override
    public DocumentCollectionResponseDTO findByCotizacionId(Integer cotizacionId) {
        DocumentCollection documentoCobranza = documentoCobranzaRepository.findByCotizacionId(cotizacionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Documento de cobranza no encontrado para cotización ID: " + cotizacionId));
        return documentoCobranzaMapper.toResponseDTO(documentoCobranza);
    }

    @Override
    @Transactional
    public DocumentCollectionResponseDTO patchDocumento(Long id, DocumentCollectionUpdateDTO documentoCobranzaUpdateDTO) {
        if (!documentoCobranzaRepository.existsById(id))
            throw new ResourceNotFoundException("Documento de cobranza no encontrado con ID: " + id);

        if (documentoCobranzaUpdateDTO.getDetailDocumentId() != null &&
                !detalleDocumentoRepository.existsById(documentoCobranzaUpdateDTO.getDetailDocumentId()))
            throw new ResourceNotFoundException(
                    "Detalle de documento no encontrado con ID: " + documentoCobranzaUpdateDTO.getDetailDocumentId());

        DocumentCollection documentoCobranza = documentoCobranzaRepository.findById(id).get();
        documentoCobranzaMapper.updateEntityFromUpdateDTO(documentoCobranza, documentoCobranzaUpdateDTO);

        // ========== LÓGICA MUTUAMENTE EXCLUYENTE: DetalleDocumento XOR PersonaJuridica
        // ========== Si se envía detalleDocumentoId, usar documento personal y LIMPIAR empresa
        if (documentoCobranzaUpdateDTO.getDetailDocumentId() != null) {
            DetailDocument detalleDocumento = detalleDocumentoRepository
                    .findById(documentoCobranzaUpdateDTO.getDetailDocumentId()).get();
            documentoCobranza.setDetailDocument(detalleDocumento);
            // Limpiar PersonaJuridica cuando se selecciona documento personal
            documentoCobranza.setPersonJuridic(null);
        }
        // Si se envía personaJuridicaId, usar empresa y LIMPIAR documento personal
        else if (documentoCobranzaUpdateDTO.getPersonJuridicId() != null) {
            PersonJuridic personaJuridica = personaJuridicaRepository
                    .findById(documentoCobranzaUpdateDTO.getPersonJuridicId())
                    .orElseThrow(() -> new ResourceNotFoundException("Persona jurídica no encontrada con ID: "
                            + documentoCobranzaUpdateDTO.getPersonJuridicId()));

            // Validar que la PersonaJuridica esté asociada a la PersonaNatural del documento
            if (documentoCobranza.getPerson() != null) {
                Integer personaId = documentoCobranza.getPerson().getId();
                PersonNatural personaNatural = personaNaturalRepository.findByPersonasId(personaId)
                        .orElse(null);

                if (personaNatural != null) {
                    boolean relacionExiste = naturalJuridicoRepository
                            .findByPersonaNaturalIdAndPersonaJuridicaId(personaNatural.getId(),
                                    documentoCobranzaUpdateDTO.getPersonJuridicId())
                            .isPresent();

                    if (!relacionExiste) {
                        throw new IllegalArgumentException(
                                "La persona jurídica ID " + documentoCobranzaUpdateDTO.getPersonJuridicId() +
                                        " no está asociada a la persona natural del documento");
                    }
                }
            }

            documentoCobranza.setPersonJuridic(personaJuridica);
            // Limpiar DetalleDocumento cuando se selecciona empresa
            documentoCobranza.setDetailDocument(null);
        }

        // Validar y actualizar Sucursal si fue proporcionada
        if (documentoCobranzaUpdateDTO.getBranchId() != null) {
            Branch sucursal = sucursalRepository.findById(documentoCobranzaUpdateDTO.getBranchId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Sucursal no encontrada con ID: " + documentoCobranzaUpdateDTO.getBranchId()));
            documentoCobranza.setBranch(sucursal);
        }

        // Validar y actualizar FormaPago si fue proporcionada
        if (documentoCobranzaUpdateDTO.getMethodPaymentId() != null) {
            MethodPayment formaPago = formaPagoRepository.findById(documentoCobranzaUpdateDTO.getMethodPaymentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Forma de pago no encontrada con ID: " + documentoCobranzaUpdateDTO.getMethodPaymentId()));
            documentoCobranza.setMethodPayment(formaPago);
            System.out.println("FormaPago actualizada: " + formaPago.getDescription());
        }

        return documentoCobranzaMapper.toResponseDTO(documentoCobranzaRepository.save(documentoCobranza));
    }

    // ========== MÉTODOS PRIVADOS ==========
    private String[] generateNextDocumentNumber() {
        Optional<DocumentCollection> lastDocOpt = documentoCobranzaRepository.findTopByOrderByIdDesc();

        if (lastDocOpt.isPresent()) {
            DocumentCollection lastDoc = lastDocOpt.get();
            String lastSerie = lastDoc.getSerie();
            Integer lastCorrelativo = lastDoc.getCorrelative();

            // Si correlativo llegó al máximo, incrementa serie
            if (lastCorrelativo >= 999999999) {
                int serieNum = Integer.parseInt(lastSerie.substring(2)) + 1;
                return new String[] { "DC" + String.format("%02d", serieNum), "1" };
            }
            // Incrementa correlativo
            return new String[] { lastSerie, String.valueOf(lastCorrelativo + 1) };
        }

        // Primer documento
        return new String[] { "DC01", "1" };
    }

    private List<DocumentCollectionResponseDTO> mapToResponseList(List<DocumentCollection> documentos) {
        return documentos.stream().map(documentoCobranzaMapper::toResponseDTO).toList();
    }

    /**
     * Obtiene el nombre del usuario autenticado actualmente
     * 
     * @return Nombre del usuario autenticado o "Usuario desconocido" si no hay autenticación
     */
    private String getAuthenticatedUserName() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()
                    && authentication.getPrincipal() instanceof UserPrincipal) {

                UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

                if (userPrincipal.getUser() != null && userPrincipal.getUser().getName() != null) {
                    return userPrincipal.getUser().getName();
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener el usuario autenticado: " + e.getMessage());
        }

        return "Usuario desconocido";
    }

    private void crearDetallesDesdeCotizacion(DocumentCollection documentoCobranza, Integer cotizacionId) {
        // Obtener todos los detalles de la cotización
        List<DetailQuotationResponseDto> detallesCotizacion = detalleCotizacionService
                .findByCotizacionId(cotizacionId);

        // Filtrar solo los detalles seleccionados
        List<DetailQuotationResponseDto> detallesSeleccionados = detallesCotizacion.stream()
                .filter(detalle -> detalle.getSelected() != null && detalle.getSelected())
                .toList();

        // Por cada detalle seleccionado, crear N detalles de documento de cobranza (donde N = cantidad)
        for (DetailQuotationResponseDto detalleCot : detallesSeleccionados) {
            int cantidad = detalleCot.getQuantity() != null ? detalleCot.getQuantity() : 1;

            // Crear un detalle de documento de cobranza por cada unidad de cantidad
            for (int i = 0; i < cantidad; i++) {
                DetailDocumentCollection detalleDoc = new DetailDocumentCollection();

                // Asignar el documento de cobranza
                detalleDoc.setDocumentCollection(documentoCobranza);

                // Mapear datos desde el detalle de cotización
                detalleDoc.setAmount(1); // Cada registro individual tiene cantidad = 1
                detalleDoc.setDescription(detalleCot.getDescription());
                detalleDoc.setPrice(
                        detalleCot.getPriceHistory() != null ? detalleCot.getPriceHistory() : BigDecimal.ZERO);

                // Asignar producto si existe
                if (detalleCot.getProduct() != null) {
                    Product producto = new Product();
                    producto.setId(detalleCot.getProduct().getId());
                    detalleDoc.setProduct(producto);
                }

                // Guardar el detalle
                detalleDocumentoCobranzaRepository.save(detalleDoc);
            }
        }
    }

    // Implementación de métodos para gestión de carpetas

    @Override
    public List<DocumentCollectionResponseDTO> findByCarpeta(Integer carpetaId) {
        return mapToResponseList(documentoCobranzaRepository.findByCarpetaId(carpetaId));
    }

    @Override
    public List<DocumentCollectionResponseDTO> findSinCarpeta() {
        return mapToResponseList(documentoCobranzaRepository.findByCarpetaIsNull());
    }

    @Override
    @Transactional
    public DocumentCollectionResponseDTO updateCarpeta(Long id, Integer carpetaId) {
        DocumentCollection documentoCobranza = documentoCobranzaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Documento de cobranza no encontrado con ID: " + id));

        if (carpetaId != null) {
            Folder carpeta = carpetaRepository.findById(carpetaId)
                    .orElseThrow(() -> new ResourceNotFoundException("Carpeta no encontrada con ID: " + carpetaId));
            documentoCobranza.setFolder(carpeta);
        } else {
            documentoCobranza.setFolder(null);
        }

        return documentoCobranzaMapper.toResponseDTO(documentoCobranzaRepository.save(documentoCobranza));
    }
}