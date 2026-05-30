package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SucursalRepository extends JpaRepository<Branch, Integer> {
    List<Branch> findByDescriptionContainingIgnoreCase(String descripcion);
    Optional<Branch> findByDescriptionIgnoreCase(String descripcion);
    List<Branch> findByStatus(Boolean estado);
    List<Branch> findByStatusAndDescriptionContainingIgnoreCase(Boolean estado, String descripcion);
    List<Branch> findByAddressContainingIgnoreCase(String direccion);
    Optional<Branch> findByMail(String email);
    boolean existsByMail(String email);

    boolean existsByDescription(String descripcion);
}
