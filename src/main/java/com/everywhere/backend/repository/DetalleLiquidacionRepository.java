package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.DetailLiquidation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DetalleLiquidacionRepository extends JpaRepository<DetailLiquidation, Integer> {

    @Query("SELECT d FROM DetailLiquidation d " +
           "LEFT JOIN FETCH d.liquidation " +
           "LEFT JOIN FETCH d.traveler " +
           "LEFT JOIN FETCH d.product " +
           "LEFT JOIN FETCH d.supplier " +
           "LEFT JOIN FETCH d.operator")
    List<DetailLiquidation> findAllWithRelations();

    @Query("SELECT d FROM DetailLiquidation d " +
           "LEFT JOIN FETCH d.liquidation " +
           "LEFT JOIN FETCH d.traveler " +
           "LEFT JOIN FETCH d.product " +
           "LEFT JOIN FETCH d.supplier " +
           "LEFT JOIN FETCH d.operator " +
           "WHERE d.id = ?1")
    Optional<DetailLiquidation> findByIdWithRelations(@Param("id") Integer id);

   @Query("SELECT d FROM DetailLiquidation d " +
           "LEFT JOIN FETCH d.liquidation " +
           "LEFT JOIN FETCH d.traveler " +
           "LEFT JOIN FETCH d.product " +
           "LEFT JOIN FETCH d.supplier " +
           "LEFT JOIN FETCH d.operator " +
           "WHERE d.liquidation.id = ?1")
    List<DetailLiquidation> findByLiquidacionIdWithRelations(@Param("liquidacionId") Integer liquidacionId);

    @Query("SELECT d FROM DetailLiquidation d " +
           "LEFT JOIN FETCH d.traveler v " +
           "LEFT JOIN FETCH v.personNatural " +
           "LEFT JOIN FETCH d.product " +
           "LEFT JOIN FETCH d.supplier " +
           "LEFT JOIN FETCH d.operator " +
           "WHERE d.liquidation.id = ?1")
    List<DetailLiquidation> findByLiquidacionIdSinLiquidacion(@Param("liquidacionId") Integer liquidacionId);

    long countByProductId(Integer productoId);

    long countBySupplierId(Integer proveedorId);

}