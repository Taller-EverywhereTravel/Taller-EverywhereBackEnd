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
    
    @Query("SELECT d FROM DetalleRecibo d WHERE d.recibo.id = :reciboId")
    List<DetailReceipt> findByReciboId(@Param("reciboId") Integer reciboId);
    
    @Query("SELECT d FROM DetalleRecibo d WHERE d.producto.id = :productoId")
    List<DetailReceipt> findByProductoId(@Param("productoId") Long productoId);

    // Métodos sin lazy loading
    @Query("SELECT DISTINCT d FROM DetalleRecibo d " +
           "LEFT JOIN FETCH d.recibo " +
           "LEFT JOIN FETCH d.producto")
    List<DetailReceipt> findAllWithRelations();

    @Query("SELECT DISTINCT d FROM DetalleRecibo d " +
           "LEFT JOIN FETCH d.recibo " +
           "LEFT JOIN FETCH d.producto " +
           "WHERE d.id = :id")
    Optional<DetailReceipt> findByIdWithRelations(@Param("id") Integer id);

    @Query("SELECT DISTINCT d FROM DetalleRecibo d " +
           "LEFT JOIN FETCH d.recibo " +
           "LEFT JOIN FETCH d.producto " +
           "WHERE d.recibo.id = :reciboId")
    List<DetailReceipt> findByReciboIdWithRelations(@Param("reciboId") Integer reciboId);
}
