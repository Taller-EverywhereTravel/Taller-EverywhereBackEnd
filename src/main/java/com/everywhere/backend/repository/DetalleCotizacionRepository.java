package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.DetailQuotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleCotizacionRepository extends JpaRepository<DetailQuotation, Integer> {
    List<DetailQuotation> findByQuotationId(int cotizacionId);

    long countByProductId(@Param("productoId") Integer productoId);

    long countBySupplierId(@Param("proveedorId") Integer proveedorId);

    long countByCategoryId(@Param("categoriaId") Integer categoriaId);


}