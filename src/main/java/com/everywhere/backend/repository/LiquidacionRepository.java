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
            "product",
            "methodPayment",
            "quotation",
            "quotation.counter",
            "quotation.statusQuotation",
            "quotation.methodPayment",
            "quotation.persons", 
            "quotation.branch",
            "quotation.folder",
            "folder",
            "observacionesLiquidacion" // Se mantiene igual porque así estaba en tu Entidad
    })
    @NonNull
    List<Liquidation> findAll();

    @EntityGraph(attributePaths = {
            "product",
            "methodPayment",
            "quotation",
            "quotation.counter",
            "quotation.statusQuotation",
            "quotation.methodPayment",
            "quotation.persons",
            "quotation.branch",
            "quotation.folder",
            "folder",
            "observacionesLiquidacion"
    })
    @NonNull
    Optional<Liquidation> findById(@NonNull Integer id);

    @EntityGraph(attributePaths = {
            "product",
            "methodPayment",
            "quotation",
            "quotation.counter",
            "quotation.statusQuotation",
            "quotation.methodPayment",
            "quotation.persons",
            "quotation.branch",
            "quotation.folder",
            "folder",
            "observacionesLiquidacion"
    })
    List<Liquidation> findByFolderId(Integer folderId);

    // MAGIA: Carpeta -> Folder
    @EntityGraph(attributePaths = {
            "product",
            "methodPayment",
            "quotation",
            "quotation.counter",
            "quotation.statusQuotation",
            "quotation.methodPayment",
            "quotation.persons",
            "quotation.branch",
            "quotation.folder",
            "folder",
            "observacionesLiquidacion"
    })
    List<Liquidation> findByFolderIsNull();
}