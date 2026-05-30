package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.PaymentPax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagoPaxRepository extends JpaRepository<PaymentPax, Integer> {

    // 1. Mantenemos @Query por los JOIN FETCH, usamos la entidad correcta y ?1
    @Query("SELECT p FROM PaymentPax p " +
           "LEFT JOIN FETCH p.liquidation " +
           "LEFT JOIN FETCH p.methodPayment " +
           "WHERE p.id = ?1")
    Optional<PaymentPax> findByIdWithRelations(Integer id);

    // 2. Mantenemos @Query por los JOIN FETCH, traducimos entidad y relaciones
    @Query("SELECT p FROM PaymentPax p " +
           "LEFT JOIN FETCH p.liquidation " +
           "LEFT JOIN FETCH p.methodPayment")
    List<PaymentPax> findAllWithRelations();

    List<PaymentPax> findByLiquidationId(Integer liquidacionId);
}
