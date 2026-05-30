package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProveedorRepository extends JpaRepository<Supplier, Integer> {
    boolean existsByRuc(Integer ruc);
}