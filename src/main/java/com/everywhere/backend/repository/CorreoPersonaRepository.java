package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.MailPerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CorreoPersonaRepository extends JpaRepository<MailPerson,Integer> {
    List<MailPerson> findByPersonaId(Integer personaId);
    boolean existsByEmail(String email);

}
