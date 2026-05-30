package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.Traveler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List; 

@Repository
public interface ViajeroRepository extends JpaRepository<Traveler, Integer> {

    // 1. Eliminamos @Param, usamos ?1, y traducimos el método a Nationality
    @Query(value = "SELECT * FROM viajeros WHERE UPPER(TRANSLATE(via_nacio_vac, 'ÁÉÍÓÚáéíóú', 'AEIOUaeiou')) LIKE UPPER(TRANSLATE(?1, 'ÁÉÍÓÚáéíóú', 'AEIOUaeiou'))", nativeQuery = true)
    List<Traveler> findByNationalityIgnoreAccents(String nacionalidad);

    // 2. Eliminamos @Param, usamos ?1, y traducimos el método a Residence
    @Query(value = "SELECT * FROM viajeros WHERE UPPER(TRANSLATE(via_resi_vac, 'ÁÉÍÓÚáéíóú', 'AEIOUaeiou')) LIKE UPPER(TRANSLATE(?1, 'ÁÉÍÓÚáéíóú', 'AEIOUaeiou'))", nativeQuery = true)
    List<Traveler> findByResidenceIgnoreAccents(String residencia);
}