package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.PhonePerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TelefonoPersonaRepository extends JpaRepository<PhonePerson, Integer> {

    List<PhonePerson> findByNumeroContaining(String numero);
    List<PhonePerson> findByCodigoPais(String codigoPais);
    List<PhonePerson> findByPersonaId(Integer personaId);
    Optional<PhonePerson> findByIdAndPersonaId(Integer telefonoId, Integer personaId);
    boolean existsByIdAndPersonaId(Integer telefonoId, Integer personaId);
}
