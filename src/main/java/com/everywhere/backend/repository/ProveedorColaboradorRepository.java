package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.SupplierCollaborator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorColaboradorRepository extends JpaRepository<SupplierCollaborator, Integer> {

    List<SupplierCollaborator> findByProveedorId(Integer proveedorId);

    List<SupplierCollaborator> findByNombreContainingIgnoreCase(String nombre);

    Optional<SupplierCollaborator> findByEmail(String email);
}
