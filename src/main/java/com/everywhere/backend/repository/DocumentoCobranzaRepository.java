package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.DocumentCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentoCobranzaRepository extends JpaRepository<DocumentCollection, Long> {

       // Busca el último documento de cobranza para generar el siguiente serie y
       // correlativo
       Optional<DocumentCollection> findTopByOrderByIdDesc();

      @Query("SELECT d FROM DocumentCollection d " +
              "LEFT JOIN FETCH d.folder " +
              "LEFT JOIN FETCH d.methodPayment " +
              "LEFT JOIN FETCH d.user " +
              "LEFT JOIN FETCH d.branch " +
              "LEFT JOIN FETCH d.person " +
              "LEFT JOIN FETCH d.detail " + 
              "LEFT JOIN FETCH d.quotation " +
              "WHERE d.serie = ?1 AND d.correlative = ?2")
       Optional<DocumentCollection> findBySerieAndCorrelative(String serie, Integer correlativo);

       Optional<DocumentCollection> findByPersonId(Long personaId);

       Optional<DocumentCollection> findByQuotationId(Integer cotizacionId);

       @Query("SELECT DISTINCT d FROM DocumentCollection d " +
              "LEFT JOIN FETCH d.folder " +
              "LEFT JOIN FETCH d.methodPayment fp " +
              "LEFT JOIN FETCH d.user " +
              "LEFT JOIN FETCH d.branch " +
              "LEFT JOIN FETCH d.person " +
              "LEFT JOIN FETCH d.personJuridic " +
              "LEFT JOIN FETCH d.detailDocument " +
              "LEFT JOIN FETCH d.quotation " +
              "WHERE d.id = ?1")
       Optional<DocumentCollection> findByIdWithRelations(Long id);

       // 6. Traducción completa al inglés en el HQL
       @Query("SELECT d FROM DocumentCollection d " +
              "LEFT JOIN FETCH d.folder " +
              "LEFT JOIN FETCH d.methodPayment " +
              "LEFT JOIN FETCH d.user " +
              "LEFT JOIN FETCH d.branch " +
              "LEFT JOIN FETCH d.person " +
              "LEFT JOIN FETCH d.quotation")
       List<DocumentCollection> findAllWithRelations();

       // 7. Traducción completa al inglés en el HQL
       @Query("SELECT DISTINCT d FROM DocumentCollection d " +
              "LEFT JOIN FETCH d.methodPayment " +
              "LEFT JOIN FETCH d.branch " +
              "LEFT JOIN FETCH d.person " +
              "LEFT JOIN FETCH d.personJuridic " +
              "LEFT JOIN FETCH d.quotation")
       List<DocumentCollection> findAllForListing();

       // 8. Traducción de relaciones y eliminación de @Param (usamos ?1)
       @Query("SELECT DISTINCT d FROM DocumentCollection d " +
              "LEFT JOIN FETCH d.detail det " +
              "LEFT JOIN FETCH det.product " +
              "WHERE d.id = ?1")
       Optional<DocumentCollection> findByIdWithDetalles(Long id);

       // ----------------------------------------------------------------------
       // MÉTODOS PARA GESTIÓN DE CARPETAS (Requieren @Query por el JOIN FETCH)
       // ----------------------------------------------------------------------

       // 9. Se usa ?1 en lugar de :carpetaId
       @Query("SELECT d FROM DocumentCollection d " +
              "LEFT JOIN FETCH d.folder " +
              "LEFT JOIN FETCH d.methodPayment " +
              "LEFT JOIN FETCH d.user " +
              "LEFT JOIN FETCH d.branch " +
              "LEFT JOIN FETCH d.person " +
              "LEFT JOIN FETCH d.quotation " +
              "WHERE d.folder.id = ?1")
       List<DocumentCollection> findByCarpetaId(Integer carpetaId);

       // 10. d.carpeta IS NULL -> d.folder IS NULL
       @Query("SELECT d FROM DocumentCollection d " +
              "LEFT JOIN FETCH d.folder " +
              "LEFT JOIN FETCH d.methodPayment " +
              "LEFT JOIN FETCH d.user " +
              "LEFT JOIN FETCH d.branch " +
              "LEFT JOIN FETCH d.person " +
              "LEFT JOIN FETCH d.quotation " +
              "WHERE d.folder IS NULL")
       List<DocumentCollection> findByCarpetaIsNull();
}