package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.DetailDocumentCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DetalleDocumentoCobranzaRepository extends JpaRepository<DetailDocumentCollection, Long> {

    List<DetailDocumentCollection> findByDocumentCollectionId(Long documentoId);
    
    List<DetailDocumentCollection> findByProductId(Long productoId);

    @Query("SELECT DISTINCT d FROM DetailDocumentCollection d " +
           "LEFT JOIN FETCH d.documentCollection " +
           "LEFT JOIN FETCH d.product")
    List<DetailDocumentCollection> findAllWithRelations();

    @Query("SELECT DISTINCT d FROM DetailDocumentCollection d " +
           "LEFT JOIN FETCH d.documentCollection " +
           "LEFT JOIN FETCH d.product " +
           "WHERE d.id = ?1")
    Optional<DetailDocumentCollection> findByIdWithRelations(Long id);

    @Query("SELECT DISTINCT d FROM DetailDocumentCollection d " +
           "LEFT JOIN FETCH d.documentCollection " +
           "LEFT JOIN FETCH d.product " +
           "WHERE d.documentCollection.id = ?1")
    List<DetailDocumentCollection> findByDocumentCollectionIdWithRelations(Long documentoId);
}