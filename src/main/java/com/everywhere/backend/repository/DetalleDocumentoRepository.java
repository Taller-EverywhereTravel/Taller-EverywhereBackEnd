package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.DetailDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DetalleDocumentoRepository extends JpaRepository<DetailDocument, Integer> {
    List<DetailDocument> findByDocumentId(Integer documentoId);
    List<DetailDocument> findByNumberContainingIgnoreCase(String numero);
    List<DetailDocument> findByPersonNaturalId(Integer personaNaturalId);
    List<DetailDocument> findByNumberStartingWithIgnoreCase(String numero);
    
    @Query("SELECT DISTINCT dd FROM DetailDocument dd " +
           "LEFT JOIN FETCH dd.document " +
           "LEFT JOIN FETCH dd.personNatural pn " +
           "LEFT JOIN FETCH pn.person")
    List<DetailDocument> findAllWithPersonasAndDocumento();
    
    @Query("SELECT DISTINCT dd FROM DetailDocument dd " +
           "LEFT JOIN FETCH dd.document " +
           "LEFT JOIN FETCH dd.personNatural pn " +
           "LEFT JOIN FETCH pn.person " +
           "WHERE LOWER(dd.number) LIKE LOWER(CONCAT(?1, '%'))")
    List<DetailDocument> findByNumberContainingWithPersonAndDocument(String numero);
}