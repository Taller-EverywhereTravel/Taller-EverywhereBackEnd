package com.everywhere.backend.service;

import com.everywhere.backend.model.dto.DetailReceiptRequestDTO;
import com.everywhere.backend.model.dto.DetailReceiptResponseDTO;

import java.util.List;

public interface DetalleReciboService {
    List<DetailReceiptResponseDTO> findAll();
    DetailReceiptResponseDTO findById(Integer id);
    List<DetailReceiptResponseDTO> findByReciboId(Integer reciboId);
    DetailReceiptResponseDTO save(DetailReceiptRequestDTO detalleReciboRequestDTO);
    DetailReceiptResponseDTO patch(Integer id, DetailReceiptRequestDTO detalleReciboRequestDTO);
    void deleteById(Integer id);
}
