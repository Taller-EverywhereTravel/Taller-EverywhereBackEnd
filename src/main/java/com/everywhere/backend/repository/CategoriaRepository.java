package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Category, Integer> {
    boolean existsByNameIgnoreCase(String nombre);
}
