package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.PersonNatural;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonaNaturalRepository extends JpaRepository<PersonNatural, Integer> {
    Optional<PersonNatural> findByDocumentIgnoreCase(String documento);
    Optional<PersonNatural> findByPersonId(Integer personaId);

    @Query(value = "SELECT * FROM persona_natural WHERE UPPER(TRANSLATE(per_nat_nomb_vac, 'ÁÉÍÓÚáéíóú', 'AEIOUaeiou')) LIKE UPPER(TRANSLATE(?1, 'ÁÉÍÓÚáéíóú', 'AEIOUaeiou'))", nativeQuery = true)
    List<PersonNatural> findByNombresIgnoreAccents(String nombres);

    @Query(value = "SELECT * FROM persona_natural WHERE UPPER(TRANSLATE(per_nat_apell_pat_vac, 'ÁÉÍÓÚáéíóú', 'AEIOUaeiou')) LIKE UPPER(TRANSLATE(?1, 'ÁÉÍÓÚáéíóú', 'AEIOUaeiou'))", nativeQuery = true)
    List<PersonNatural> findByApellidosPaternoIgnoreAccents(String apellidosPaterno);

    @Query(value = "SELECT * FROM persona_natural WHERE UPPER(TRANSLATE(per_nat_apell_mat_vac, 'ÁÉÍÓÚáéíóú', 'AEIOUaeiou')) LIKE UPPER(TRANSLATE(?1, 'ÁÉÍÓÚáéíóú', 'AEIOUaeiou'))", nativeQuery = true)
    List<PersonNatural> findByApellidosMaternoIgnoreAccents(String apellidosMaterno);

    List<PersonNatural> findByCategoryPersonId(Integer categoriaId);
    Optional<PersonNatural> findByDocumentIgnoreCaseAndIdNot(String documento, Integer id);
    long countByCategoryPersonId(Integer categoriaId);
}