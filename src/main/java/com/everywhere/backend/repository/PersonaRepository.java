package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Person, Integer> {
    List<Person> findByMailPrimaryContainingIgnoreCase(String email);
    @Query("""
           SELECT p 
           FROM Person p 
           JOIN p.phone t 
           WHERE LOWER(t.number) LIKE LOWER(CONCAT('%', ?1, '%'))
           """)
    List<Person> findByTelefonoContainingIgnoreCase(String telefono);

    @Query("""
           SELECT p 
           FROM Person p 
           JOIN p.mail m 
           WHERE LOWER(m.mail) LIKE LOWER(CONCAT('%', ?1, '%'))
           """)
    List<Person> findByMailAddressContainingIgnoreCase(String mailAddress);

    @Query("SELECT p FROM Person p LEFT JOIN FETCH p.phone WHERE p.id = ?1")
    Optional<Person> findByIdWithTelefonos(Integer id);
}