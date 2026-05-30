package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.PhonePerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TelefonoPersonaRepository extends JpaRepository<PhonePerson, Integer> {

    List<PhonePerson> findByNumberContaining(String numero);
    List<PhonePerson> findByCodeCountry(String codigoPais);
    List<PhonePerson> findByPersonId(Integer personaId);
    Optional<PhonePerson> findByIdAndPersonId(Integer telefonoId, Integer personaId);
    boolean existsByIdAndPersonId(Integer telefonoId, Integer personaId);
}
