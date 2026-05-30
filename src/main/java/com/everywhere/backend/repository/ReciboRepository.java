package com.everywhere.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.everywhere.backend.model.entity.Receipt;

@Repository
public interface ReciboRepository extends JpaRepository<Receipt, Integer> {

        // Busca el último recibo para generar el siguiente serie y correlativo
        Optional<Receipt> findTopByOrderByIdDesc();

        @Query("SELECT r FROM Receipt r " +
               "LEFT JOIN FETCH r.folder " +
               "LEFT JOIN FETCH r.methodPayment " +
               "LEFT JOIN FETCH r.user " +
               "LEFT JOIN FETCH r.branch " +
               "LEFT JOIN FETCH r.person " +
               "LEFT JOIN FETCH r.detailReceipt " + 
               "LEFT JOIN FETCH r.quotation " +
               "WHERE r.serie = ?1 AND r.correlative = ?2")
        Optional<Receipt> findBySerieAndCorrelative(String serie, Integer correlativo);

        Optional<Receipt> findByPersonId(Long personaId);

        Optional<Receipt> findByQuotationId(Integer cotizacionId);

        @Query("SELECT DISTINCT r FROM Receipt r " +
               "LEFT JOIN FETCH r.folder " +
               "LEFT JOIN FETCH r.methodPayment fp " +
               "LEFT JOIN FETCH r.user " +
               "LEFT JOIN FETCH r.branch " +
               "LEFT JOIN FETCH r.person " +
               "LEFT JOIN FETCH r.personJuridic " +
               "LEFT JOIN FETCH r.detailDocument " +
               "LEFT JOIN FETCH r.quotation " +
               "WHERE r.id = ?1")
        Optional<Receipt> findByIdWithRelations(Integer id);

        // 6. Traducción completa al inglés en el HQL
        @Query("SELECT r FROM Receipt r " +
               "LEFT JOIN FETCH r.folder " +
               "LEFT JOIN FETCH r.methodPayment " +
               "LEFT JOIN FETCH r.user " +
               "LEFT JOIN FETCH r.branch " +
               "LEFT JOIN FETCH r.person " +
               "LEFT JOIN FETCH r.quotation")
        List<Receipt> findAllWithRelations();

        // 7. Traducción completa al inglés en el HQL
        @Query("SELECT DISTINCT r FROM Receipt r " +
               "LEFT JOIN FETCH r.methodPayment " +
               "LEFT JOIN FETCH r.branch " +
               "LEFT JOIN FETCH r.person " +
               "LEFT JOIN FETCH r.personJuridic " +
               "LEFT JOIN FETCH r.quotation")
        List<Receipt> findAllForListing();

        // 8. Traducción de relaciones y eliminación de @Param (usamos ?1)
        @Query("SELECT DISTINCT r FROM Receipt r " +
               "LEFT JOIN FETCH r.detailReceipt det " +
               "LEFT JOIN FETCH det.product " +
               "WHERE r.id = ?1")
        Optional<Receipt> findByIdWithDetalles(Integer id);

        // ----------------------------------------------------------------------
        // MÉTODOS PARA GESTIÓN DE CARPETAS
        // ----------------------------------------------------------------------

        // 9. Se usa ?1 en lugar de :carpetaId
        @Query("SELECT r FROM Receipt r " +
               "LEFT JOIN FETCH r.folder " +
               "LEFT JOIN FETCH r.methodPayment " +
               "LEFT JOIN FETCH r.user " +
               "LEFT JOIN FETCH r.branch " +
               "LEFT JOIN FETCH r.person " +
               "LEFT JOIN FETCH r.quotation " +
               "WHERE r.folder.id = ?1")
        List<Receipt> findByCarpetaId(Integer carpetaId);

        // 10. r.carpeta IS NULL -> r.folder IS NULL
        @Query("SELECT r FROM Receipt r " +
               "LEFT JOIN FETCH r.folder " +
               "LEFT JOIN FETCH r.methodPayment " +
               "LEFT JOIN FETCH r.user " +
               "LEFT JOIN FETCH r.branch " +
               "LEFT JOIN FETCH r.person " +
               "LEFT JOIN FETCH r.quotation " +
               "WHERE r.folder IS NULL")
        List<Receipt> findByCarpetaIsNull();
}
