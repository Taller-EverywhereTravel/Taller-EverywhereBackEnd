package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.DetailReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DetalleReciboRepository extends JpaRepository<DetailReceipt, Integer> {
    
    List<DetailReceipt> findByReceiptId(Integer reciboId);
    
    List<DetailReceipt> findByProductId(Long productoId);

    @Query("SELECT DISTINCT d FROM DetailReceipt d " +
           "LEFT JOIN FETCH d.receipt " +
           "LEFT JOIN FETCH d.product")
    List<DetailReceipt> findAllWithRelations();

    // Se eliminó @Param y se usa ?1
    @Query("SELECT DISTINCT d FROM DetailReceipt d " +
           "LEFT JOIN FETCH d.receipt " +
           "LEFT JOIN FETCH d.product " +
           "WHERE d.id = ?1")
    Optional<DetailReceipt> findByIdWithRelations(Integer id);

    // Se eliminó @Param, se usa ?1 y d.recibo.id -> d.receipt.id
    @Query("SELECT DISTINCT d FROM DetailReceipt d " +
           "LEFT JOIN FETCH d.receipt " +
           "LEFT JOIN FETCH d.product " +
           "WHERE d.receipt.id = ?1")
    List<DetailReceipt> findByReciboIdWithRelations(Integer reciboId);
}
