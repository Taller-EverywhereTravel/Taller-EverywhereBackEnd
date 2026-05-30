package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.NaturalJuridic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NaturalJuridicoRepository extends JpaRepository<NaturalJuridic, Integer> {

    List<NaturalJuridic> findByPersonNaturalId(Integer personaNaturalId);

    List<NaturalJuridic> findByPersonJuridicId(Integer personaJuridicaId);

    Optional<NaturalJuridic> findByPersonNaturalIdAndPersonJuridicId( // Verificar si ya existe una relación específica
        Integer personaNaturalId, 
        Integer personaJuridicaId
    );

    void deleteByPersonNaturalIdAndPersonJuridicId(Integer personaNaturalId, Integer personaJuridicaId);
}