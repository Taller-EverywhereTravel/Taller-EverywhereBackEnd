package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.CategoryPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoriaPersonaRepository extends JpaRepository<CategoryPerson, Integer> {
    
    // Buscar por nombre (case insensitive)
    List<CategoryPerson> findByNameContainingIgnoreCase(String nombre);
    
    Optional<CategoryPerson> findByNameIgnoreCase(String nombre);
    boolean existsByNameIgnoreCase(String nombre);
}
