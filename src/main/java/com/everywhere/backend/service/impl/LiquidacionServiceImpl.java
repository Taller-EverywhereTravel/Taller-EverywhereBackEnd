package com.everywhere.backend.service.impl;

import com.everywhere.backend.model.dto.LiquidationRequestDTO;
import com.everywhere.backend.model.dto.LiquidationResponseDTO;
import com.everywhere.backend.model.dto.LiquidationWithDetailResponseDTO;
import com.everywhere.backend.model.dto.DetailLiquidationResponseDTO;
import com.everywhere.backend.model.dto.DetailLiquidationSimpleDTO;
import com.everywhere.backend.model.dto.DetailQuotationResponseDto;
import com.everywhere.backend.model.dto.PaymentPaxResponseDTO;
import com.everywhere.backend.model.entity.Folder;
import com.everywhere.backend.model.entity.Quotation;
import com.everywhere.backend.model.entity.DetailLiquidation;
import com.everywhere.backend.model.entity.MethodPayment;
import com.everywhere.backend.model.dto.ObservationLiquidationResponseDTO;
import com.everywhere.backend.model.dto.ObservationLiquidationSimpleDTO;
import com.everywhere.backend.model.entity.Liquidation;
import com.everywhere.backend.model.entity.Product;
import com.everywhere.backend.repository.CarpetaRepository;
import com.everywhere.backend.repository.CotizacionRepository;
import com.everywhere.backend.repository.DetalleLiquidacionRepository;
import com.everywhere.backend.repository.FormaPagoRepository;
import com.everywhere.backend.repository.LiquidacionRepository;
import com.everywhere.backend.repository.ProductoRepository;
import com.everywhere.backend.service.DetalleCotizacionService;
import com.everywhere.backend.service.LiquidacionService;
import com.everywhere.backend.service.DetalleLiquidacionService;
import com.everywhere.backend.service.ObservacionLiquidacionService;
import com.everywhere.backend.service.PagoPaxService;
import com.everywhere.backend.exceptions.ResourceNotFoundException;
import com.everywhere.backend.mapper.LiquidacionMapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LiquidacionServiceImpl implements LiquidacionService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final LiquidacionRepository liquidacionRepository;
    private final LiquidacionMapper liquidacionMapper;
    private final DetalleLiquidacionService detalleLiquidacionService;
    private final DetalleLiquidacionRepository detalleLiquidacionRepository;
    private final DetalleCotizacionService detalleCotizacionService;
    private final CotizacionRepository cotizacionRepository;
    private final CarpetaRepository carpetaRepository;
    private final ObservacionLiquidacionService observacionLiquidacionService;
    private final PagoPaxService pagoPaxService;
    private final FormaPagoRepository formaPagoRepository;
    private final ProductoRepository productoRepository;

    @Override
    public List<LiquidationResponseDTO> findAll() {
        return liquidacionRepository.findAll().stream().map(liquidacionMapper::toResponseDTO).toList();
    }

    @Override
    public LiquidationResponseDTO findById(Integer id) {
        Liquidation liquidacion = liquidacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Liquidación no encontrada con ID: " + id));
        return liquidacionMapper.toResponseDTO(liquidacion);
    }

    @Override
    @Transactional
    public LiquidationResponseDTO update(Integer id, LiquidationRequestDTO liquidacionRequestDTO) {
        if (!liquidacionRepository.existsById(id))
            throw new ResourceNotFoundException("Liquidación no encontrada con ID: " + id);

        if (liquidacionRequestDTO.getQuotationId() != null &&
                !cotizacionRepository.existsById(liquidacionRequestDTO.getQuotationId()))
            throw new ResourceNotFoundException(
                    "Cotización no encontrada con ID: " + liquidacionRequestDTO.getQuotationId());

        if (liquidacionRequestDTO.getProductId() != null &&
                !productoRepository.existsById(liquidacionRequestDTO.getProductId()))
            throw new ResourceNotFoundException(
                    "Producto no encontrado con ID: " + liquidacionRequestDTO.getProductId());

        if (liquidacionRequestDTO.getMethodPaymentId() != null &&
                !formaPagoRepository.existsById(liquidacionRequestDTO.getMethodPaymentId()))
            throw new ResourceNotFoundException(
                    "Forma de pago no encontrada con ID: " + liquidacionRequestDTO.getMethodPaymentId());

        if (liquidacionRequestDTO.getFolderId() != null &&
                !carpetaRepository.existsById(liquidacionRequestDTO.getFolderId()))
            throw new ResourceNotFoundException(
                    "Carpeta no encontrada con ID: " + liquidacionRequestDTO.getFolderId());

        Liquidation liquidacion = liquidacionRepository.findById(id).get();
        liquidacionMapper.updateEntityFromRequest(liquidacion, liquidacionRequestDTO);

        if (liquidacionRequestDTO.getQuotationId() != null) {
            Quotation cotizacion = cotizacionRepository.findById(liquidacionRequestDTO.getQuotationId()).get();
            liquidacion.setQuotation(cotizacion);
        }

        if (liquidacionRequestDTO.getProductId() != null) {
            Product producto = productoRepository.findById(liquidacionRequestDTO.getProductId()).get();
            liquidacion.setProduct(producto);
        }

        if (liquidacionRequestDTO.getMethodPaymentId() != null) {
            MethodPayment formaPago = formaPagoRepository.findById(liquidacionRequestDTO.getMethodPaymentId()).get();
            liquidacion.setMethodPayment(formaPago);
        }

        if (liquidacionRequestDTO.getFolderId() != null) {
            Folder carpeta = carpetaRepository.findById(liquidacionRequestDTO.getFolderId()).get();
            liquidacion.setFolder(carpeta);
        }

        return liquidacionMapper.toResponseDTO(liquidacionRepository.save(liquidacion));
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        if (!liquidacionRepository.existsById(id))
            throw new ResourceNotFoundException("Liquidación no encontrada con ID: " + id);
        liquidacionRepository.deleteById(id);
    }

    @Override
    public LiquidationWithDetailResponseDTO findByIdWithDetalles(Integer id) {
        Liquidation liquidacion = liquidacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Liquidación no encontrada con ID: " + id));

        LiquidationResponseDTO liquidacionResponseDTO = liquidacionMapper.toResponseDTO(liquidacion);

        List<DetailLiquidationResponseDTO> detalleLiquidacionResponseDTOs = detalleLiquidacionService
                .findByLiquidacionId(id);
        List<DetailLiquidationSimpleDTO> detalleLiquidacionSimpleDTOs = detalleLiquidacionResponseDTOs.stream()
                .map(this::convertirADetalleSimple).toList();

        List<ObservationLiquidationResponseDTO> observacionLiquidacionResponseDTOS = observacionLiquidacionService
                .findByLiquidacionId(id);
        List<ObservationLiquidationSimpleDTO> observacionLiquidacionSimpleDTOs = observacionLiquidacionResponseDTOS
                .stream().map(this::convertirAObservacionSimple).toList();

        LiquidationWithDetailResponseDTO liquidacionConDetallesResponseDTO = new LiquidationWithDetailResponseDTO();
        liquidacionConDetallesResponseDTO.setId(liquidacionResponseDTO.getId());
        liquidacionConDetallesResponseDTO.setNumber(liquidacionResponseDTO.getNumber());
        liquidacionConDetallesResponseDTO.setDatePurchase(liquidacionResponseDTO.getDatePurchase());
        liquidacionConDetallesResponseDTO.setDestiny(liquidacionResponseDTO.getDestiny());
        liquidacionConDetallesResponseDTO.setNumberPassenger(liquidacionResponseDTO.getNumberPassenger());
        liquidacionConDetallesResponseDTO.setCreated(liquidacionResponseDTO.getCreated());
        liquidacionConDetallesResponseDTO.setUpdated(liquidacionResponseDTO.getUpdated());
        liquidacionConDetallesResponseDTO.setProduct(liquidacionResponseDTO.getProduct());
        liquidacionConDetallesResponseDTO.setMethodPayment(liquidacionResponseDTO.getMethodPayment());
        liquidacionConDetallesResponseDTO.setDetail(detalleLiquidacionSimpleDTOs);
        liquidacionConDetallesResponseDTO.setObservation(observacionLiquidacionSimpleDTOs);

        return liquidacionConDetallesResponseDTO;
    }

    @Override
    public ByteArrayInputStream generateExcel(Integer liquidacionId) {
        LiquidationWithDetailResponseDTO liquidacion = findByIdWithDetalles(liquidacionId);
        List<PaymentPaxResponseDTO> pagosPax = pagoPaxService.findByLiquidacionId(liquidacionId);

        try (Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle moneyStyle = createMoneyStyle(workbook);

            createResumenSheet(workbook, liquidacion, pagosPax, headerStyle, moneyStyle);
            createDetallesSheet(workbook, liquidacion.getDetail(), headerStyle, moneyStyle);
            createObservacionesSheet(workbook, liquidacion.getObservation(), headerStyle, moneyStyle);
            createPagosPaxSheet(workbook, pagosPax, headerStyle, moneyStyle);

            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException("Error al generar el Excel de la liquidación", e);
        }
    }

    private void createResumenSheet(Workbook workbook, LiquidationWithDetailResponseDTO liquidacion,
            List<PaymentPaxResponseDTO> pagosPax, CellStyle headerStyle, CellStyle moneyStyle) {
        Sheet sheet = workbook.createSheet("Resumen");

        Row headerRow = sheet.createRow(0);
        Cell campoHeader = headerRow.createCell(0);
        campoHeader.setCellValue("Campo");
        campoHeader.setCellStyle(headerStyle);
        Cell valorHeader = headerRow.createCell(1);
        valorHeader.setCellValue("Valor");
        valorHeader.setCellStyle(headerStyle);

        int rowIndex = 1;
        addResumenValue(sheet, rowIndex++, "Número", toText(liquidacion.getNumber()));
        addResumenValue(sheet, rowIndex++, "Fecha Compra", formatDate(liquidacion.getDatePurchase()));
        addResumenValue(sheet, rowIndex++, "Destino", toText(liquidacion.getDestiny()));
        addResumenValue(sheet, rowIndex++, "Número de Pasajeros", toText(liquidacion.getNumberPassenger()));
        addResumenValue(sheet, rowIndex++, "Producto", getLiquidacionProducto(liquidacion));
        addResumenValue(sheet, rowIndex++, "Forma de Pago", getLiquidacionFormaPago(liquidacion));
        addResumenValue(sheet, rowIndex++, "Creado", formatDateTime(liquidacion.getCreated()));
        addResumenValue(sheet, rowIndex++, "Actualizado", formatDateTime(liquidacion.getUpdated()));

        rowIndex++;

        addResumenMoneyValue(sheet, rowIndex++, "Total Costo Ticket",
                sumDetalles(liquidacion.getDetail(), DetailLiquidationSimpleDTO::getCostTicket), moneyStyle);
        addResumenMoneyValue(sheet, rowIndex++, "Total Cargo Servicio",
                sumDetalles(liquidacion.getDetail(), DetailLiquidationSimpleDTO::getChargeService), moneyStyle);
        addResumenMoneyValue(sheet, rowIndex++, "Total Valor Venta",
                sumDetalles(liquidacion.getDetail(), DetailLiquidationSimpleDTO::getValueSale), moneyStyle);
        addResumenMoneyValue(sheet, rowIndex++, "Total Monto Descuento",
                sumDetalles(liquidacion.getDetail(), DetailLiquidationSimpleDTO::getAmountDiscount), moneyStyle);
        addResumenMoneyValue(sheet, rowIndex++, "Total Pagos PAX USD",
                sumPagosPaxByMoneda(pagosPax, "USD"), moneyStyle);
        addResumenMoneyValue(sheet, rowIndex++, "Total Pagos PAX PEN",
                sumPagosPaxByMoneda(pagosPax, "PEN"), moneyStyle);
        addResumenValue(sheet, rowIndex, "Cantidad de Pagos PAX", toText(pagosPax == null ? 0 : pagosPax.size()));

        autoSizeColumns(sheet, 2);
    }

    private void createDetallesSheet(Workbook workbook, List<DetailLiquidationSimpleDTO> detalles, CellStyle headerStyle,
            CellStyle moneyStyle) {
        Sheet sheet = workbook.createSheet("Detalles");

        String[] headers = {
                "#",
                "Viajero",
                "Producto",
                "Proveedor",
                "Operador",
                "Ticket",
                "Doc. Cobro",
                "Costo Ticket",
                "Cargo Servicio",
                "Valor Venta",
                "Fee Emisión",
                "Doc. Fee",
                "Comisión",
                "Factura Compra",
                "Boleta Pasajero",
                "Monto Descuento",
                "Creado",
                "Actualizado"
        };

        createHeaderRow(sheet, headers, headerStyle);

        if (detalles == null || detalles.isEmpty()) {
            Row emptyRow = sheet.createRow(1);
            emptyRow.createCell(0).setCellValue("Sin detalles");
            autoSizeColumns(sheet, headers.length);
            return;
        }

        int rowIndex = 1;
        int nro = 1;
        for (DetailLiquidationSimpleDTO detalle : detalles) {
            Row row = sheet.createRow(rowIndex++);
            int col = 0;

            row.createCell(col++).setCellValue(nro++);
            row.createCell(col++).setCellValue(getViajeroNombre(detalle));
            row.createCell(col++).setCellValue(getProductoNombre(detalle));
            row.createCell(col++).setCellValue(detalle.getSupplier() != null ? toText(detalle.getSupplier().getName()) : "");
            row.createCell(col++).setCellValue(detalle.getOperator() != null ? toText(detalle.getOperator().getName()) : "");
            row.createCell(col++).setCellValue(toText(detalle.getTicket()));
            row.createCell(col++).setCellValue(toText(detalle.getDocumentCollection()));
            setMoneyCell(row, col++, detalle.getCostTicket(), moneyStyle);
            setMoneyCell(row, col++, detalle.getChargeService(), moneyStyle);
            setMoneyCell(row, col++, detalle.getValueSale(), moneyStyle);
            row.createCell(col++).setCellValue(toText(detalle.getFeeEmision()));
            row.createCell(col++).setCellValue(toText(detalle.getDocumentFee()));
            row.createCell(col++).setCellValue(toText(detalle.getComission()));
            row.createCell(col++).setCellValue(toText(detalle.getInvoicePurchase()));
            row.createCell(col++).setCellValue(toText(detalle.getTicketPassenger()));
            setMoneyCell(row, col++, detalle.getAmountDiscount(), moneyStyle);
            row.createCell(col++).setCellValue(formatDateTime(detalle.getCreated()));
            row.createCell(col).setCellValue(formatDateTime(detalle.getUpdated()));
        }

        Row totalRow = sheet.createRow(rowIndex);
        totalRow.createCell(0).setCellValue("TOTALES");
        setMoneyCell(totalRow, 7, sumDetalles(detalles, DetailLiquidationSimpleDTO::getCostTicket), moneyStyle);
        setMoneyCell(totalRow, 8, sumDetalles(detalles, DetailLiquidationSimpleDTO::getChargeService), moneyStyle);
        setMoneyCell(totalRow, 9, sumDetalles(detalles, DetailLiquidationSimpleDTO::getValueSale), moneyStyle);
        setMoneyCell(totalRow, 15, sumDetalles(detalles, DetailLiquidationSimpleDTO::getAmountDiscount), moneyStyle);

        autoSizeColumns(sheet, headers.length);
    }

    private void createObservacionesSheet(Workbook workbook,
            List<ObservationLiquidationSimpleDTO> observaciones,
            CellStyle headerStyle,
            CellStyle moneyStyle) {
        Sheet sheet = workbook.createSheet("Observaciones");
        String[] headers = {
                "#",
                "Descripción",
                "Creado",
                "Actualizado"
        };

        createHeaderRow(sheet, headers, headerStyle);

        if (observaciones == null || observaciones.isEmpty()) {
            Row emptyRow = sheet.createRow(1);
            emptyRow.createCell(0).setCellValue("Sin observaciones");
            autoSizeColumns(sheet, headers.length);
            return;
        }

        int rowIndex = 1;
        int nro = 1;
        for (ObservationLiquidationSimpleDTO observacion : observaciones) {
            Row row = sheet.createRow(rowIndex++);
            int col = 0;
            row.createCell(col++).setCellValue(nro++);
            row.createCell(col++).setCellValue(toText(observacion.getDescription()));
            row.createCell(col++).setCellValue(formatDateTime(observacion.getCreated()));
            row.createCell(col).setCellValue(formatDateTime(observacion.getUpdated()));
        }

        autoSizeColumns(sheet, headers.length);
    }

    private void createPagosPaxSheet(Workbook workbook,
            List<PaymentPaxResponseDTO> pagosPax,
            CellStyle headerStyle,
            CellStyle moneyStyle) {
        Sheet sheet = workbook.createSheet("Pagos PAX");
        String[] headers = {
                "#",
                "Monto",
                "Moneda",
                "Detalle",
                "Forma de Pago",
                "Creado",
                "Actualizado"
        };

        createHeaderRow(sheet, headers, headerStyle);

        if (pagosPax == null || pagosPax.isEmpty()) {
            Row emptyRow = sheet.createRow(1);
            emptyRow.createCell(0).setCellValue("Sin pagos PAX");
            autoSizeColumns(sheet, headers.length);
            return;
        }

        int rowIndex = 1;
        int nro = 1;
        for (PaymentPaxResponseDTO pagoPax : pagosPax) {
            Row row = sheet.createRow(rowIndex++);
            int col = 0;
            row.createCell(col++).setCellValue(nro++);
            setMoneyCell(row, col++, pagoPax.getAmount(), moneyStyle);
            row.createCell(col++).setCellValue(toText(pagoPax.getCurrency()));
            row.createCell(col++).setCellValue(toText(pagoPax.getDetail()));
            row.createCell(col++).setCellValue(pagoPax.getMethodPayment() != null ? toText(pagoPax.getMethodPayment().getDescription()) : "");
            row.createCell(col++).setCellValue(formatDateTime(pagoPax.getCreated()));
            row.createCell(col).setCellValue(formatDateTime(pagoPax.getUpdated()));
        }

        autoSizeColumns(sheet, headers.length);
    }

    private void createHeaderRow(Sheet sheet, String[] headers, CellStyle headerStyle) {
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private void addResumenValue(Sheet sheet, int rowIndex, String label, String value) {
        Row row = sheet.createRow(rowIndex);
        row.createCell(0).setCellValue(label);
        row.createCell(1).setCellValue(value);
    }

    private void addResumenMoneyValue(Sheet sheet, int rowIndex, String label, BigDecimal value, CellStyle moneyStyle) {
        Row row = sheet.createRow(rowIndex);
        row.createCell(0).setCellValue(label);
        setMoneyCell(row, 1, value, moneyStyle);
    }

    private void setMoneyCell(Row row, int columnIndex, BigDecimal value, CellStyle moneyStyle) {
        Cell cell = row.createCell(columnIndex);
        cell.setCellValue(value != null ? value.doubleValue() : 0D);
        cell.setCellStyle(moneyStyle);
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);

        return style;
    }

    private CellStyle createMoneyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
        return style;
    }

    private void autoSizeColumns(Sheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private BigDecimal sumDetalles(List<DetailLiquidationSimpleDTO> detalles,
            Function<DetailLiquidationSimpleDTO, BigDecimal> extractor) {
        if (detalles == null || detalles.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return detalles.stream()
                .map(extractor)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal sumPagosPaxByMoneda(List<PaymentPaxResponseDTO> pagosPax, String moneda) {
        if (pagosPax == null || pagosPax.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return pagosPax.stream()
                .filter(pago -> pago.getCurrency() != null && pago.getCurrency().trim().toUpperCase(Locale.ROOT)
                        .equals(moneda.toUpperCase(Locale.ROOT)))
                .map(PaymentPaxResponseDTO::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String getLiquidacionProducto(LiquidationWithDetailResponseDTO liquidacion) {
        if (liquidacion.getProduct() == null) {
            return "";
        }
        if (liquidacion.getProduct().getType() != null && !liquidacion.getProduct().getType().isBlank()) {
            return liquidacion.getProduct().getType();
        }
        return toText(liquidacion.getProduct().getDescription());
    }

    private String getLiquidacionFormaPago(LiquidationWithDetailResponseDTO liquidacion) {
        if (liquidacion.getMethodPayment() == null) {
            return "";
        }
        return toText(liquidacion.getMethodPayment().getDescription());
    }

    private String getViajeroNombre(DetailLiquidationSimpleDTO detalle) {
        if (detalle.getTraveler() == null || detalle.getTraveler().getPersonNatural() == null) {
            return "";
        }

        String nombres = toText(detalle.getTraveler().getPersonNatural().getName());
        String apellidoPaterno = toText(detalle.getTraveler().getPersonNatural().getSurnamePaternal());
        String apellidoMaterno = toText(detalle.getTraveler().getPersonNatural().getSurnameMaternal());

        return (nombres + " " + apellidoPaterno + " " + apellidoMaterno).trim().replaceAll("\\s+", " ");
    }

    private String getProductoNombre(DetailLiquidationSimpleDTO detalle) {
        if (detalle.getProduct() == null) {
            return "";
        }
        if (detalle.getProduct().getType() != null && !detalle.getProduct().getType().isBlank()) {
            return detalle.getProduct().getType();
        }
        return toText(detalle.getProduct().getDescription());
    }

    private String toText(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    private String formatDate(LocalDate date) {
        return date == null ? "" : date.format(DATE_FORMATTER);
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime == null ? "" : dateTime.format(DATE_TIME_FORMATTER);
    }

    private DetailLiquidationSimpleDTO convertirADetalleSimple(
            DetailLiquidationResponseDTO detalleLiquidacionResponseDTO) {
        DetailLiquidationSimpleDTO detalleLiquidacionSimpleDTO = new DetailLiquidationSimpleDTO();
        detalleLiquidacionSimpleDTO.setId(detalleLiquidacionResponseDTO.getId());
        detalleLiquidacionSimpleDTO.setTicket(detalleLiquidacionResponseDTO.getTicket());
        detalleLiquidacionSimpleDTO.setDocumentCollection(detalleLiquidacionResponseDTO.getDocumentCollection());
        detalleLiquidacionSimpleDTO.setCostTicket(detalleLiquidacionResponseDTO.getCostTicket());
        detalleLiquidacionSimpleDTO.setChargeService(detalleLiquidacionResponseDTO.getChargeService());
        detalleLiquidacionSimpleDTO.setValueSale(detalleLiquidacionResponseDTO.getValueSale());
        detalleLiquidacionSimpleDTO.setFeeEmision(detalleLiquidacionResponseDTO.getFeeEmision());
        detalleLiquidacionSimpleDTO.setDocumentFee(detalleLiquidacionResponseDTO.getDocumentFee());
        detalleLiquidacionSimpleDTO.setComission(detalleLiquidacionResponseDTO.getComission());
        detalleLiquidacionSimpleDTO.setInvoicePurchase(detalleLiquidacionResponseDTO.getInvoicePurchase());
        detalleLiquidacionSimpleDTO.setTicketPassenger(detalleLiquidacionResponseDTO.getTicketPassenger());
        detalleLiquidacionSimpleDTO.setAmountDiscount(detalleLiquidacionResponseDTO.getAmountDiscount());
        detalleLiquidacionSimpleDTO.setPaymentPaxUSD(detalleLiquidacionResponseDTO.getPaymentPaxUSD());
        detalleLiquidacionSimpleDTO.setPaymentPaxPEN(detalleLiquidacionResponseDTO.getPaymentPaxPEN());
        detalleLiquidacionSimpleDTO.setCreated(detalleLiquidacionResponseDTO.getCreated());
        detalleLiquidacionSimpleDTO.setUpdated(detalleLiquidacionResponseDTO.getUpdated());

        detalleLiquidacionSimpleDTO.setTraveler(detalleLiquidacionResponseDTO.getTraveler());
        detalleLiquidacionSimpleDTO.setProduct(detalleLiquidacionResponseDTO.getProduct());
        detalleLiquidacionSimpleDTO.setSupplier(detalleLiquidacionResponseDTO.getSupplier());
        detalleLiquidacionSimpleDTO.setOperator(detalleLiquidacionResponseDTO.getOperator());

        return detalleLiquidacionSimpleDTO;
    }

    @Override
    @Transactional
    public LiquidationResponseDTO create(LiquidationRequestDTO liquidacionRequestDTO, Integer cotizacionId) {
        if (!cotizacionRepository.existsById(cotizacionId))
            throw new ResourceNotFoundException("Cotización no encontrada con ID: " + cotizacionId);

        if (liquidacionRequestDTO.getProductId() != null &&
                !productoRepository.existsById(liquidacionRequestDTO.getProductId()))
            throw new ResourceNotFoundException(
                    "Producto no encontrado con ID: " + liquidacionRequestDTO.getProductId());

        if (liquidacionRequestDTO.getMethodPaymentId() != null &&
                !formaPagoRepository.existsById(liquidacionRequestDTO.getMethodPaymentId()))
            throw new ResourceNotFoundException(
                    "Forma de pago no encontrada con ID: " + liquidacionRequestDTO.getMethodPaymentId());

        if (liquidacionRequestDTO.getFolderId() != null &&
                !carpetaRepository.existsById(liquidacionRequestDTO.getFolderId()))
            throw new ResourceNotFoundException(
                    "Carpeta no encontrada con ID: " + liquidacionRequestDTO.getFolderId());

        Quotation cotizacion = cotizacionRepository.findById(cotizacionId).get();

        Liquidation liquidacion = liquidacionMapper.toEntity(liquidacionRequestDTO);
        liquidacion.setQuotation(cotizacion);

        if (liquidacionRequestDTO.getProductId() != null) {
            Product producto = productoRepository.findById(liquidacionRequestDTO.getProductId()).get();
            liquidacion.setProduct(producto);
        }

        if (liquidacionRequestDTO.getMethodPaymentId() != null) {
            MethodPayment formaPago = formaPagoRepository.findById(liquidacionRequestDTO.getMethodPaymentId()).get();
            liquidacion.setMethodPayment(formaPago);
        }

        if (liquidacionRequestDTO.getFolderId() != null) {
            Folder carpeta = carpetaRepository.findById(liquidacionRequestDTO.getFolderId()).get();
            liquidacion.setFolder(carpeta);
        }

        liquidacion = liquidacionRepository.save(liquidacion); // Guardar la liquidación primero para obtener el ID
        crearDetallesDesdeCotizacion(liquidacion, cotizacionId);
        return liquidacionMapper.toResponseDTO(liquidacion);
    }

    /**
     * Método privado que crea los detalles de liquidación basándose en los detalles de la cotización.
     * Implementa la lógica de repartición: por cada detalle seleccionado de la cotización,
     * crea N detalles de liquidación donde N = cantidad del detalle de cotización.
     */
    private void crearDetallesDesdeCotizacion(Liquidation liquidacion, Integer cotizacionId) {
        // Obtener todos los detalles de la cotizacion
        List<DetailQuotationResponseDto> detallesCotizacion = detalleCotizacionService
                .findByCotizacionId(cotizacionId);

        // Filtrar solo los detalles seleccionados
        List<DetailQuotationResponseDto> detallesSeleccionados = detallesCotizacion.stream()
                .filter(detalle -> detalle.getSelected() != null && detalle.getSelected())
                .toList();

        // Por cada detalle seleccionado, crear N detalles de liquidación (donde N = cantidad)
        for (DetailQuotationResponseDto detalleCot : detallesSeleccionados) {
            int cantidad = detalleCot.getQuantity() != null ? detalleCot.getQuantity() : 1;

            // Crear un detalle de liquidación por cada unidad de cantidad
            for (int i = 0; i < cantidad; i++) {
                DetailLiquidation detalleLiq = new DetailLiquidation();

                // Asignar la liquidación
                detalleLiq.setLiquidation(liquidacion);

                // Mapear datos desde el detalle de cotización
                detalleLiq.setCostTicket(
                        detalleCot.getPriceHistory() != null ? detalleCot.getPriceHistory() : BigDecimal.ZERO);
                detalleLiq.setChargeService(
                        detalleCot.getComission() != null ? detalleCot.getComission() : BigDecimal.ZERO);

                // Asignar producto y proveedor si existen
                if (detalleCot.getProduct() != null) {
                    detalleLiq.setProduct(detalleCot.getProduct());
                }
                if (detalleCot.getSupplier() != null) {
                    detalleLiq.setSupplier(detalleCot.getSupplier());
                }

                // Inicializar otros campos con valores por defecto (se llenarán después)
                detalleLiq.setTicket("");
                detalleLiq.setValueSale(BigDecimal.ZERO);
                detalleLiq.setInvoicePurchase("");
                detalleLiq.setTicketPassenger("");
                detalleLiq.setAmountDiscount(BigDecimal.ZERO);
                detalleLiq.setPaymentPaxUSD(BigDecimal.ZERO);
                detalleLiq.setPaymentPaxPEN(BigDecimal.ZERO);
                // Viajero y Operador se quedan null para ser asignados después
                
                detalleLiquidacionRepository.save(detalleLiq);
            }
        }
    }

    private ObservationLiquidationSimpleDTO convertirAObservacionSimple(
            ObservationLiquidationResponseDTO observacionLiquidacionResponseDTO) {
        ObservationLiquidationSimpleDTO observacionLiquidacionSimpleDTO = new ObservationLiquidationSimpleDTO();
        observacionLiquidacionSimpleDTO.setId(observacionLiquidacionResponseDTO.getId());
        observacionLiquidacionSimpleDTO.setDescription(observacionLiquidacionResponseDTO.getDescription());
        observacionLiquidacionSimpleDTO.setValue(observacionLiquidacionResponseDTO.getValue());
        observacionLiquidacionSimpleDTO.setDocument(observacionLiquidacionResponseDTO.getDocument());
        observacionLiquidacionSimpleDTO.setNumberDocument(observacionLiquidacionResponseDTO.getNumberDocument());
        observacionLiquidacionSimpleDTO.setCreated(observacionLiquidacionResponseDTO.getCreated());
        observacionLiquidacionSimpleDTO.setUpdated(observacionLiquidacionResponseDTO.getUpdated());
        return observacionLiquidacionSimpleDTO;
    }

    // Implementación de métodos para gestión de carpetas

    @Override
    public List<LiquidationResponseDTO> findByCarpeta(Integer carpetaId) {
        if (!carpetaRepository.existsById(carpetaId))
            throw new ResourceNotFoundException("Carpeta no encontrada con ID: " + carpetaId);

        return liquidacionRepository.findByCarpetaId(carpetaId).stream()
                .map(liquidacionMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<LiquidationResponseDTO> findSinCarpeta() {
        return liquidacionRepository.findByCarpetaIsNull().stream()
                .map(liquidacionMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public LiquidationResponseDTO updateCarpeta(Integer id, Integer carpetaId) {
        Liquidation liquidacion = liquidacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Liquidación no encontrada con ID: " + id));

        if (carpetaId != null) {
            // Asociar a una carpeta
            Folder carpeta = carpetaRepository.findById(carpetaId)
                    .orElseThrow(() -> new ResourceNotFoundException("Carpeta no encontrada con ID: " + carpetaId));
            liquidacion.setFolder(carpeta);
        } else {
            // Desasociar de la carpeta
            liquidacion.setFolder(null);
        }

        return liquidacionMapper.toResponseDTO(liquidacionRepository.save(liquidacion));
    }
}