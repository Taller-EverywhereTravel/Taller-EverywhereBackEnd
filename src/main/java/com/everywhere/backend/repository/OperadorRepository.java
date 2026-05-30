package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.Operator; 
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OperadorRepository extends JpaRepository<Operator, Integer> {
    Optional<Operator> findByName(String nombre);
    List<Operator> name(String nombre);
    boolean existsByNameIgnoreCase(String nombre);
}