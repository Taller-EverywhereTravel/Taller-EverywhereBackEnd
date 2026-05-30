package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.Traveler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List; 

@Repository
public interface ViajeroRepository extends JpaRepository<Traveler, Integer> {

    @Query(value = "SELECT * FROM viajeros WHERE UPPER(TRANSLATE(via_nacio_vac, 'ÁÉÍÓÚáéíóú', 'AEIOUaeiou')) LIKE UPPER(TRANSLATE(:nacionalidad, 'ÁÉÍÓÚáéíóú', 'AEIOUaeiou'))", nativeQuery = true)
    List<Traveler> findByNacionalidadIgnoreAccents(@Param("nacionalidad") String nacionalidad);

    @Query(value = "SELECT * FROM viajeros WHERE UPPER(TRANSLATE(via_resi_vac, 'ÁÉÍÓÚáéíóú', 'AEIOUaeiou')) LIKE UPPER(TRANSLATE(:residencia, 'ÁÉÍÓÚáéíóú', 'AEIOUaeiou'))", nativeQuery = true)
    List<Traveler> findByResidenciaIgnoreAccents(@Param("residencia") String residencia);
}