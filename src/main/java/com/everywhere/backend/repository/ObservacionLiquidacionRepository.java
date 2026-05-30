package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.ObservationLiquidation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ObservacionLiquidacionRepository extends JpaRepository<ObservationLiquidation, Long> {

    // 1. Traducción de entidad, relaciones y uso de ?1 en lugar de :id
    @Query("SELECT o FROM ObservationLiquidation o " +
           "LEFT JOIN FETCH o.liquidation l " +
           "LEFT JOIN FETCH l.product " +
           "LEFT JOIN FETCH l.methodPayment " +
           "LEFT JOIN FETCH l.quotation " +
           "LEFT JOIN FETCH l.folder " +
           "WHERE o.id = ?1")
    Optional<ObservationLiquidation> findByIdWithLiquidacion(Long id);

    // 2. Traducción de entidad y relaciones
    @Query("SELECT o FROM ObservationLiquidation o " +
           "LEFT JOIN FETCH o.liquidation l " +
           "LEFT JOIN FETCH l.product " +
           "LEFT JOIN FETCH l.methodPayment " +
           "LEFT JOIN FETCH l.quotation " +
           "LEFT JOIN FETCH l.folder")
    List<ObservationLiquidation> findAllWithLiquidacion();

    // 3. Traducción de entidad, relaciones y uso de ?1 en lugar de :liquidacionId
    @Query("SELECT o FROM ObservationLiquidation o " +
           "LEFT JOIN FETCH o.liquidation " +
           "WHERE o.liquidation.id = ?1")
    List<ObservationLiquidation> findByLiquidacionId(Integer liquidacionId);
}