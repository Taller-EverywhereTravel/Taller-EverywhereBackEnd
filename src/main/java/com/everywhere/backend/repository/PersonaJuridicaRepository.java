package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.PersonJuridic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonaJuridicaRepository extends JpaRepository<PersonJuridic, Integer> {
    Optional<PersonJuridic> findByRucIgnoreCase(String ruc);

    @Query(value = "SELECT * FROM persona_juridica WHERE UPPER(TRANSLATE(per_jurd_razSocial_vac, 'ÁÉÍÓÚáéíóú', 'AEIOUaeiou')) LIKE UPPER(TRANSLATE(?1, 'ÁÉÍÓÚáéíóú', 'AEIOUaeiou'))", nativeQuery = true)
    List<PersonJuridic> findByRazonSocialIgnoreAccents(String razonSocial);

    // Método original mantenido para compatibilidad
    List<PersonJuridic> findByNameCompanyIgnoreCase(String razonSocial);
    Optional<PersonJuridic> findByPersonId(Integer personaId);
    Optional<PersonJuridic> findByRucIgnoreCaseAndIdNot(String ruc, Integer id);
}