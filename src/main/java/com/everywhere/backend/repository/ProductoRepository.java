package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Product, Integer> {
    boolean existsProductByType(String tipo);
}