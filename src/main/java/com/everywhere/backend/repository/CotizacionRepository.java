package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CotizacionRepository extends JpaRepository<Quotation, Integer> {
    @Query("SELECT MAX(q.id) FROM Quotation q")
    Integer findMaxId();

    @Query("SELECT q FROM Quotation q WHERE q.id NOT IN (SELECT l.quotation.id FROM Liquidation l WHERE l.quotation IS NOT NULL)")
    List<Quotation> findQuotationWithoutLiquidation();

    long countByMethodPaymentId(Integer formaPagoId);

    long countByStatusQuotationId(int estado);

    List<Quotation> findByid(int id);

    // Métodos para gestión de carpetas
    List<Quotation> findByFolderId(Integer carpetaId);

    List<Quotation> findByFolderIsNull();
}