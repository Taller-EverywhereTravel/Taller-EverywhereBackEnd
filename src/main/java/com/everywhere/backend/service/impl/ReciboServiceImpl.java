package com.everywhere.backend.service.impl;

import com.everywhere.backend.model.dto.QuotationWithDetailResponseDTO;
import com.everywhere.backend.model.dto.ReceiptResponseDTO;
import com.everywhere.backend.model.dto.ReceiptUpdateDTO;
import com.everywhere.backend.exceptions.ResourceNotFoundException;
import com.everywhere.backend.mapper.ReciboMapper;
import com.everywhere.backend.model.entity.Receipt;
import com.everywhere.backend.model.entity.MethodPayment;
import com.everywhere.backend.model.entity.PersonJuridic;
import com.everywhere.backend.repository.PersonaJuridicaRepository;
import com.everywhere.backend.repository.PersonaNaturalRepository;
import com.everywhere.backend.model.entity.PersonNatural;
import com.everywhere.backend.model.entity.Branch;
import com.everywhere.backend.repository.SucursalRepository;
import com.everywhere.backend.model.entity.DetailReceipt;
import com.everywhere.backend.model.entity.DetailDocument;
import com.everywhere.backend.repository.DetalleReciboRepository;
import com.everywhere.backend.repository.DetalleDocumentoRepository;
import com.everywhere.backend.repository.ReciboRepository;
import com.everywhere.backend.repository.FormaPagoRepository;
import com.everywhere.backend.repository.NaturalJuridicoRepository;
import com.everywhere.backend.repository.ProductoRepository;
import com.everywhere.backend.model.entity.Folder;
import com.everywhere.backend.repository.CarpetaRepository;
import com.everywhere.backend.security.UserPrincipal;
import com.everywhere.backend.service.CotizacionService;
import com.everywhere.backend.service.DetalleCotizacionService;
import com.everywhere.backend.service.ReciboService;
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
public class ReciboServiceImpl implements ReciboService {

    private final CotizacionService cotizacionService;
    private final ReciboRepository reciboRepository;
    private final DetalleReciboRepository detalleReciboRepository;
    private final DetalleDocumentoRepository detalleDocumentoRepository;
    private final ReciboMapper reciboMapper;
    private final PersonaJuridicaRepository personaJuridicaRepository;
    private final SucursalRepository sucursalRepository;
    private final FormaPagoRepository formaPagoRepository;
    private final NaturalJuridicoRepository naturalJuridicoRepository;
    private final PersonaNaturalRepository personaNaturalRepository;
    private final DetalleCotizacionService detalleCotizacionService;
    private final ProductoRepository productoRepository;
    private final CarpetaRepository carpetaRepository;
    private final com.everywhere.backend.util.pdf.ReciboPdfGenerator reciboPdfGenerator;

    @Override
    @Transactional
    public ReceiptResponseDTO createRecibo(Integer cotizacionId, Integer personaJuridicaId, Integer sucursalId) {
        if (cotizacionId == null)
            throw new IllegalArgumentException("El ID de la cotización no puede ser nulo");

        if (reciboRepository.findByQuotationId(cotizacionId).isPresent())
            throw new DataIntegrityViolationException(
                    "Ya existe un recibo para la cotización ID: " + cotizacionId);

        QuotationWithDetailResponseDTO cotizacion = cotizacionService.findByIdWithDetalles(cotizacionId);

        String[] serieCorrelativo = generateNextDocumentNumber();
        Receipt recibo = reciboMapper.fromCotizacion(cotizacion, serieCorrelativo[0],
                Integer.parseInt(serieCorrelativo[1]));

        // Validar y setear PersonaJuridica si fue proporcionada
        if (personaJuridicaId != null) {
            PersonJuridic personaJuridica = personaJuridicaRepository.findById(personaJuridicaId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Persona jurídica no encontrada con ID: " + personaJuridicaId));

            // Validar que la PersonaJuridica esté asociada a la PersonaNatural de la
            // cotización
            if (cotizacion.getPerson() != null) {
                Integer personaId = cotizacion.getPerson().getId();
                PersonNatural personaNatural = personaNaturalRepository.findByPersonId(personaId)
                        .orElse(null);

                if (personaNatural != null) {
                    boolean relacionExiste = naturalJuridicoRepository
                            .findByPersonNaturalIdAndPersonJuridicId(personaNatural.getId(), personaJuridicaId)
                            .isPresent();

                    if (!relacionExiste) {
                        throw new IllegalArgumentException("La persona jurídica ID " + personaJuridicaId +
                                " no está asociada a la persona natural de la cotización");
                    }
                }
            }

            recibo.setPersonJuridic(personaJuridica);
        }

        // Validar y setear Sucursal si fue proporcionada
        if (sucursalId != null) {
            Branch sucursal = sucursalRepository.findById(sucursalId)
                    .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada con ID: " + sucursalId));
            recibo.setBranch(sucursal);
        }

        recibo = reciboRepository.save(recibo);

        // Crear detalles desde cotización
        crearDetallesDesdeCotizacion(recibo, cotizacionId);

        return reciboMapper.toResponseDTO(recibo);
    }

    @Override
    public ByteArrayInputStream generatePdf(Integer reciboId) {
        Receipt recibo = reciboRepository.findByIdWithRelations(reciboId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Recibo no encontrado con ID: " + reciboId));

        reciboRepository.findByIdWithDetalles(reciboId)
                .ifPresent(r -> recibo.setDetailReceipt(r.getDetailReceipt()));

        ReceiptResponseDTO reciboResponseDTO = reciboMapper.toResponseDTO(recibo);
        String userName = getAuthenticatedUserName();
        return reciboPdfGenerator.generatePdf(reciboResponseDTO, userName);
    }

    @Override
    public ReceiptResponseDTO findById(Integer id) {
        Receipt recibo = reciboRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recibo no encontrado con ID: " + id));
        return reciboMapper.toResponseDTO(recibo);
    }

    @Override
    public ReceiptResponseDTO findBySerieAndCorrelativo(String serie, Integer correlativo) {
        Receipt recibo = reciboRepository.findBySerieAndCorrelative(serie, correlativo)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Recibo no encontrado con serie: " + serie + " y correlativo: " + correlativo));
        return reciboMapper.toResponseDTO(recibo);
    }

    @Override
    public List<ReceiptResponseDTO> findAll() {
        return mapToResponseList(reciboRepository.findAllForListing());
    }

    @Override
    public ReceiptResponseDTO findByCotizacionId(Integer cotizacionId) {
        Receipt recibo = reciboRepository.findByQuotationId(cotizacionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Recibo no encontrado para cotización ID: " + cotizacionId));
        return reciboMapper.toResponseDTO(recibo);
    }

    @Override
    @Transactional
    public ReceiptResponseDTO patchRecibo(Integer id, ReceiptUpdateDTO reciboUpdateDTO) {
        if (!reciboRepository.existsById(id))
            throw new ResourceNotFoundException("Recibo no encontrado con ID: " + id);

        if (reciboUpdateDTO.getDetailDocumentId() != null &&
                !detalleDocumentoRepository.existsById(reciboUpdateDTO.getDetailDocumentId()))
            throw new ResourceNotFoundException(
                    "Detalle de documento no encontrado con ID: " + reciboUpdateDTO.getDetailDocumentId());

        Receipt recibo = reciboRepository.findById(id).get();
        reciboMapper.updateEntityFromUpdateDTO(recibo, reciboUpdateDTO);

        // Lógica mutuamente excluyente: DetalleDocumento XOR PersonaJuridica
        if (reciboUpdateDTO.getDetailDocumentId() != null) {
            DetailDocument detalleDocumento = detalleDocumentoRepository
                    .findById(reciboUpdateDTO.getDetailDocumentId()).get();
            recibo.setDetailDocument(detalleDocumento);
            recibo.setPersonJuridic(null);
        } else if (reciboUpdateDTO.getPersonJuridicId() != null) {
            PersonJuridic personaJuridica = personaJuridicaRepository
                    .findById(reciboUpdateDTO.getPersonJuridicId())
                    .orElseThrow(() -> new ResourceNotFoundException("Persona jurídica no encontrada con ID: "
                            + reciboUpdateDTO.getPersonJuridicId()));

            if (recibo.getPerson() != null) {
                Integer personaId = recibo.getPerson().getId();
                PersonNatural personaNatural = personaNaturalRepository.findByPersonId(personaId)
                        .orElse(null);

                if (personaNatural != null) {
                    boolean relacionExiste = naturalJuridicoRepository
                            .findByPersonNaturalIdAndPersonJuridicId(personaNatural.getId(),
                                    reciboUpdateDTO.getPersonJuridicId())
                            .isPresent();

                    if (!relacionExiste) {
                        throw new IllegalArgumentException(
                                "La persona jurídica ID " + reciboUpdateDTO.getPersonJuridicId() +
                                        " no está asociada a la persona natural del recibo");
                    }
                }
            }

            recibo.setPersonJuridic(personaJuridica);
            recibo.setDetailDocument(null);
        }

        // Validar y actualizar Sucursal
        if (reciboUpdateDTO.getBranchId() != null) {
            Branch sucursal = sucursalRepository.findById(reciboUpdateDTO.getBranchId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Sucursal no encontrada con ID: " + reciboUpdateDTO.getBranchId()));
            recibo.setBranch(sucursal);
        }

        // Validar y actualizar FormaPago
        if (reciboUpdateDTO.getMethodPaymentId() != null) {
            MethodPayment formaPago = formaPagoRepository.findById(reciboUpdateDTO.getMethodPaymentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Forma de pago no encontrada con ID: " + reciboUpdateDTO.getMethodPaymentId()));
            recibo.setMethodPayment(formaPago);
        }

        return reciboMapper.toResponseDTO(reciboRepository.save(recibo));
    }

    // ========== MÉTODOS PRIVADOS ==========
    private String[] generateNextDocumentNumber() {
        Optional<Receipt> lastReciboOpt = reciboRepository.findTopByOrderByIdDesc();

        if (lastReciboOpt.isPresent()) {
            Receipt lastRecibo = lastReciboOpt.get();
            String lastSerie = lastRecibo.getSerie();
            Integer lastCorrelativo = lastRecibo.getCorrelative();

            if (lastSerie != null && lastCorrelativo != null) {
                // Incrementar correlativo
                int nextCorrelativo = lastCorrelativo + 1;
                return new String[] { lastSerie, String.valueOf(nextCorrelativo) };
            }
        }

        // Primer recibo: serie R01, correlativo 1
        return new String[] { "R01", "1" };
    }

    @Transactional
    private void crearDetallesDesdeCotizacion(Receipt recibo, Integer cotizacionId) {
        List<DetailQuotationResponseDto> detallesCotizacion = detalleCotizacionService
                .findByCotizacionId(cotizacionId);

        for (DetailQuotationResponseDto detalleCotizacion : detallesCotizacion) {
            DetailReceipt detalleRecibo = new DetailReceipt();
            detalleRecibo.setReceipt(recibo);
            detalleRecibo.setAmount(detalleCotizacion.getQuantity() != null ? detalleCotizacion.getQuantity() : 0);
            detalleRecibo.setDescription(detalleCotizacion.getDescription());
            detalleRecibo.setPrice(
                    detalleCotizacion.getPriceHistory() != null ? detalleCotizacion.getPriceHistory()
                            : BigDecimal.ZERO);

            if (detalleCotizacion.getProduct() != null && detalleCotizacion.getProduct().getId() > 0) {
                Product producto = productoRepository.findById(detalleCotizacion.getProduct().getId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Producto no encontrado con ID: " + detalleCotizacion.getProduct().getId()));
                detalleRecibo.setProduct(producto);
            }

            detalleReciboRepository.save(detalleRecibo);
        }
    }

    private List<ReceiptResponseDTO> mapToResponseList(List<Receipt> recibos) {
        return recibos.stream().map(reciboMapper::toResponseDTO).toList();
    }

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

    // Implementación de métodos para gestión de carpetas

    @Override
    public List<ReceiptResponseDTO> findByCarpeta(Integer carpetaId) {
        return mapToResponseList(reciboRepository.findByCarpetaId(carpetaId));
    }

    @Override
    public List<ReceiptResponseDTO> findSinCarpeta() {
        return mapToResponseList(reciboRepository.findByCarpetaIsNull());
    }

    @Override
    @Transactional
    public ReceiptResponseDTO updateCarpeta(Integer id, Integer carpetaId) {
        Receipt recibo = reciboRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recibo no encontrado con ID: " + id));

        if (carpetaId != null) {
            Folder carpeta = carpetaRepository.findById(carpetaId)
                    .orElseThrow(() -> new ResourceNotFoundException("Carpeta no encontrada con ID: " + carpetaId));
            recibo.setFolder(carpeta);
        } else {
            recibo.setFolder(null);
        }

        return reciboMapper.toResponseDTO(reciboRepository.save(recibo));
    }
}
