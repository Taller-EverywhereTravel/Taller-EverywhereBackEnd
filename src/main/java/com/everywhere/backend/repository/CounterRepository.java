package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.Counter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CounterRepository extends JpaRepository<Counter, Integer> {
    Optional<Counter> findByCode(String codigo);
    Optional<Counter> findByNameIgnoreCase(String nombre);
}