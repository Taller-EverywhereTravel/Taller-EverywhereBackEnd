package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SucursalRepository extends JpaRepository<Branch, Integer> {
    List<Branch> findByDescripcionContainingIgnoreCase(String descripcion);
    Optional<Branch> findByDescripcionIgnoreCase(String descripcion);
    List<Branch> findByEstado(Boolean estado);
    List<Branch> findByEstadoAndDescripcionContainingIgnoreCase(Boolean estado, String descripcion);
    List<Branch> findByDireccionContainingIgnoreCase(String direccion);
    Optional<Branch> findByEmail(String email);
    boolean existsByEmail(String email);

    boolean existsByDescripcion(String descripcion);
}
