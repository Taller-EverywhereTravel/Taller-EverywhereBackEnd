package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.RecordQuotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistorialCotizacionRepository extends JpaRepository<RecordQuotation, Integer> {

    @Query("SELECT h FROM RecordQuotation h " +
           "LEFT JOIN FETCH h.user " +
           "LEFT JOIN FETCH h.quotation " +
           "LEFT JOIN FETCH h.statusQuotation " +
           "ORDER BY h.dateCreated DESC")
    List<RecordQuotation> findAllWithRelations();

    // Se eliminó @Param y se reemplazó :id por ?1
    @Query("SELECT h FROM RecordQuotation h " +
           "LEFT JOIN FETCH h.user " +
           "LEFT JOIN FETCH h.quotation " +
           "LEFT JOIN FETCH h.statusQuotation " +
           "WHERE h.id = ?1")
    Optional<RecordQuotation> findByIdWithRelations(Integer id);

    // Se eliminó @Param y se reemplazó :cotizacionId por ?1
    @Query("SELECT h FROM RecordQuotation h " +
           "LEFT JOIN FETCH h.user " +
           "LEFT JOIN FETCH h.statusQuotation " +
           "WHERE h.quotation.id = ?1 " +
           "ORDER BY h.dateCreated DESC")
    List<RecordQuotation> findByCotizacionIdWithRelations(Integer cotizacionId);
}