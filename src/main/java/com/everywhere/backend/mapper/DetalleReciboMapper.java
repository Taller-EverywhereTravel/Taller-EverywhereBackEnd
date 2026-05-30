package com.everywhere.backend.mapper;

import com.everywhere.backend.model.dto.DetailQuotationSimpleDTO;
import com.everywhere.backend.model.dto.DetailReceiptRequestDTO;
import com.everywhere.backend.model.dto.DetailReceiptResponseDTO;
import com.everywhere.backend.model.entity.DetailReceipt;
import com.everywhere.backend.model.entity.Receipt;
import com.everywhere.backend.model.entity.Product;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DetalleReciboMapper {
    
    private final ModelMapper modelMapper;

    public DetailReceipt toEntity(DetailReceiptRequestDTO detalleReciboRequestDTO) {
        return modelMapper.map(detalleReciboRequestDTO, DetailReceipt.class);
    }

    public DetailReceiptResponseDTO toResponseDTO(DetailReceipt detalleRecibo) {
        DetailReceiptResponseDTO dto = modelMapper.map(detalleRecibo, DetailReceiptResponseDTO.class);
        
        if (detalleRecibo.getProduct() != null) {
            dto.setProductId(detalleRecibo.getProduct().getId());
            dto.setProductDescription(detalleRecibo.getProduct().getType());
        }
        if (detalleRecibo.getReceipt() != null) {
            dto.setReceiptId(detalleRecibo.getReceipt().getId());
            dto.setReceiptNumber(
                String.format("%s-%09d", 
                    detalleRecibo.getReceipt().getSerie(),
                    detalleRecibo.getReceipt().getCorrelative()));
        }
        
        return dto;
    }

    public void updateEntityFromRequest(DetailReceipt detalleRecibo, DetailReceiptRequestDTO detalleReciboRequestDTO) {
        modelMapper.map(detalleReciboRequestDTO, detalleRecibo);
    }

    // Convierte los detalles seleccionados de una cotización a detalles de recibo
    public List<DetailReceipt> fromCotizacionDetalles(List<DetailQuotationSimpleDTO> detallesCotizacion, Receipt recibo) {
        List<DetailReceipt> detalles = new ArrayList<>();

        if (detallesCotizacion != null) {
            for (DetailQuotationSimpleDTO detalleCotizacionSimpleDTO : detallesCotizacion) {
                if (detalleCotizacionSimpleDTO.getSelected() != null && detalleCotizacionSimpleDTO.getSelected()) {
                    DetailReceipt detalleRecibo = new DetailReceipt();
                    detalleRecibo.setReceipt(recibo);
                    detalleRecibo.setAmount(detalleCotizacionSimpleDTO.getQuantity() != null ? detalleCotizacionSimpleDTO.getQuantity() : 0);
                    detalleRecibo.setDescription(detalleCotizacionSimpleDTO.getDescription());
                    detalleRecibo.setPrice(detalleCotizacionSimpleDTO.getPriceHistory() != null ? detalleCotizacionSimpleDTO.getPriceHistory() : BigDecimal.ZERO);

                    if (detalleCotizacionSimpleDTO.getProduct() != null) {
                        Product producto = new Product();
                        producto.setId(detalleCotizacionSimpleDTO.getProduct().getId());
                        detalleRecibo.setProduct(producto);
                    }
                    detalles.add(detalleRecibo);
                }
            }
        }
        return detalles;
    }
}
