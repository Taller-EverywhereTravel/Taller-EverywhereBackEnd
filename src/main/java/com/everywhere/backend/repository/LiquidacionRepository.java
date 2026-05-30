package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.Liquidation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LiquidacionRepository extends JpaRepository<Liquidation, Integer> {

    @EntityGraph(attributePaths = {
            "producto",
            "formaPago",
            "cotizacion",
            "cotizacion.counter",
            "cotizacion.estadoCotizacion",
            "cotizacion.formaPago",
            "cotizacion.personas",
            "cotizacion.sucursal",
            "cotizacion.carpeta",
            "carpeta",
            "observacionesLiquidacion"
    })
    @NonNull
    List<Liquidation> findAll();

    @EntityGraph(attributePaths = {
            "producto",
            "formaPago",
            "cotizacion",
            "cotizacion.counter",
            "cotizacion.estadoCotizacion",
            "cotizacion.formaPago",
            "cotizacion.personas",
            "cotizacion.sucursal",
            "cotizacion.carpeta",
            "carpeta",
            "observacionesLiquidacion"
    })
    @NonNull
    Optional<Liquidation> findById(@NonNull Integer id);

    // Buscar liquidaciones por carpeta
    @EntityGraph(attributePaths = {
            "producto",
            "formaPago",
            "cotizacion",
            "cotizacion.counter",
            "cotizacion.estadoCotizacion",
            "cotizacion.formaPago",
            "cotizacion.personas",
            "cotizacion.sucursal",
            "cotizacion.carpeta",
            "carpeta",
            "observacionesLiquidacion"
    })
    List<Liquidation> findByCarpetaId(Integer carpetaId);

    // Buscar liquidaciones sin carpeta asignada
    @EntityGraph(attributePaths = {
            "producto",
            "formaPago",
            "cotizacion",
            "cotizacion.counter",
            "cotizacion.estadoCotizacion",
            "cotizacion.formaPago",
            "cotizacion.personas",
            "cotizacion.sucursal",
            "cotizacion.carpeta",
            "carpeta",
            "observacionesLiquidacion"
    })
    List<Liquidation> findByCarpetaIsNull();
}