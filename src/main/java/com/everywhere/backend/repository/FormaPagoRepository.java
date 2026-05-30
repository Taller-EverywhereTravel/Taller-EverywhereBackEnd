package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.MethodPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormaPagoRepository extends JpaRepository<MethodPayment, Integer> {
    Optional<MethodPayment> findByCode(Integer codigo); 
    boolean existsByCode(Integer codigo);
    List<MethodPayment> findByDescriptionContainingIgnoreCase(String descripcion);
    Optional<MethodPayment> findByDescriptionIgnoreCase(String descripcion);
}