package com.everywhere.backend.service;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.everywhere.backend.model.dto.ReceiptResponseDTO;
import com.everywhere.backend.model.dto.ReceiptUpdateDTO;

public interface ReciboService {
    ReceiptResponseDTO createRecibo(Integer cotizacionId, Integer personaJuridicaId, Integer sucursalId);
    ByteArrayInputStream generatePdf(Integer reciboId);
    ReceiptResponseDTO findById(Integer id);
    ReceiptResponseDTO findBySerieAndCorrelativo(String serie, Integer correlativo);
    List<ReceiptResponseDTO> findAll();
    ReceiptResponseDTO findByCotizacionId(Integer cotizacionId);
    ReceiptResponseDTO patchRecibo(Integer id, ReceiptUpdateDTO reciboUpdateDTO);
    List<ReceiptResponseDTO> findByCarpeta(Integer carpetaId);
    List<ReceiptResponseDTO> findSinCarpeta();
    ReceiptResponseDTO updateCarpeta(Integer id, Integer carpetaId);
}
