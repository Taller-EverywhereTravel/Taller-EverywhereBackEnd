package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.DetailDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DetalleDocumentoRepository extends JpaRepository<DetailDocument, Integer> {
    List<DetailDocument> findByDocumentoId(Integer documentoId);
    List<DetailDocument> findByNumeroContainingIgnoreCase(String numero);
    List<DetailDocument> findByPersonaNaturalId(Integer personaNaturalId);
    List<DetailDocument> findByNumeroStartingWithIgnoreCase(String numero);
    
    @Query("SELECT DISTINCT dd FROM DetalleDocumento dd " +
           "LEFT JOIN FETCH dd.documento " +
           "LEFT JOIN FETCH dd.personaNatural pn " +
           "LEFT JOIN FETCH pn.personas")
    List<DetailDocument> findAllWithPersonasAndDocumento();
    
    @Query("SELECT DISTINCT dd FROM DetalleDocumento dd " +
           "LEFT JOIN FETCH dd.documento " +
           "LEFT JOIN FETCH dd.personaNatural pn " +
           "LEFT JOIN FETCH pn.personas " +
           "WHERE LOWER(dd.numero) LIKE LOWER(CONCAT(:numero, '%'))")
    List<DetailDocument> findByNumeroContainingWithPersonasAndDocumento(String numero);
}