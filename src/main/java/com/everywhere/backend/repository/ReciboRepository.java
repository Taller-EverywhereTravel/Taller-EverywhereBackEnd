package com.everywhere.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.everywhere.backend.model.entity.Receipt;

@Repository
public interface ReciboRepository extends JpaRepository<Receipt, Integer> {

        // Busca el último recibo para generar el siguiente serie y correlativo
        Optional<Receipt> findTopByOrderByIdDesc();

        @Query("SELECT r FROM Recibo r " +
                        "LEFT JOIN FETCH r.carpeta " +
                        "LEFT JOIN FETCH r.formaPago " +
                        "LEFT JOIN FETCH r.usuario " +
                        "LEFT JOIN FETCH r.sucursal " +
                        "LEFT JOIN FETCH r.persona " +
                        "LEFT JOIN FETCH r.detalleRecibo " +
                        "LEFT JOIN FETCH r.cotizacion " +
                        "WHERE r.serie = :serie AND r.correlativo = :correlativo")
        Optional<Receipt> findBySerieAndCorrelativo(@Param("serie") String serie,
                        @Param("correlativo") Integer correlativo);

        @Query("SELECT r FROM Recibo r WHERE r.persona.id = :personaId")
        Optional<Receipt> findByPersonaId(@Param("personaId") Long personaId);

        @Query("SELECT r FROM Recibo r WHERE r.cotizacion.id = :cotizacionId")
        Optional<Receipt> findByCotizacionId(@Param("cotizacionId") Integer cotizacionId);

        @Query("SELECT DISTINCT r FROM Recibo r " +
                        "LEFT JOIN FETCH r.carpeta " +
                        "LEFT JOIN FETCH r.formaPago fp " +
                        "LEFT JOIN FETCH r.usuario " +
                        "LEFT JOIN FETCH r.sucursal " +
                        "LEFT JOIN FETCH r.persona " +
                        "LEFT JOIN FETCH r.personaJuridica " +
                        "LEFT JOIN FETCH r.detalleDocumento " +
                        "LEFT JOIN FETCH r.cotizacion " +
                        "WHERE r.id = :id")
        Optional<Receipt> findByIdWithRelations(@Param("id") Integer id);

        @Query("SELECT r FROM Recibo r " +
                        "LEFT JOIN FETCH r.carpeta " +
                        "LEFT JOIN FETCH r.formaPago " +
                        "LEFT JOIN FETCH r.usuario " +
                        "LEFT JOIN FETCH r.sucursal " +
                        "LEFT JOIN FETCH r.persona " +
                        "LEFT JOIN FETCH r.cotizacion")
        List<Receipt> findAllWithRelations();

        @Query("SELECT DISTINCT r FROM Recibo r " +
                        "LEFT JOIN FETCH r.formaPago " +
                        "LEFT JOIN FETCH r.sucursal " +
                        "LEFT JOIN FETCH r.persona " +
                        "LEFT JOIN FETCH r.personaJuridica " +
                        "LEFT JOIN FETCH r.cotizacion")
        List<Receipt> findAllForListing();

        @Query("SELECT DISTINCT r FROM Recibo r " +
                        "LEFT JOIN FETCH r.detalleRecibo det " +
                        "LEFT JOIN FETCH det.producto " +
                        "WHERE r.id = :id")
        Optional<Receipt> findByIdWithDetalles(@Param("id") Integer id);

        // Métodos para gestión de carpetas
        @Query("SELECT r FROM Recibo r " +
                        "LEFT JOIN FETCH r.carpeta " +
                        "LEFT JOIN FETCH r.formaPago " +
                        "LEFT JOIN FETCH r.usuario " +
                        "LEFT JOIN FETCH r.sucursal " +
                        "LEFT JOIN FETCH r.persona " +
                        "LEFT JOIN FETCH r.cotizacion " +
                        "WHERE r.carpeta.id = :carpetaId")
        List<Receipt> findByCarpetaId(@Param("carpetaId") Integer carpetaId);

        @Query("SELECT r FROM Recibo r " +
                        "LEFT JOIN FETCH r.carpeta " +
                        "LEFT JOIN FETCH r.formaPago " +
                        "LEFT JOIN FETCH r.usuario " +
                        "LEFT JOIN FETCH r.sucursal " +
                        "LEFT JOIN FETCH r.persona " +
                        "LEFT JOIN FETCH r.cotizacion " +
                        "WHERE r.carpeta IS NULL")
        List<Receipt> findByCarpetaIsNull();
}
