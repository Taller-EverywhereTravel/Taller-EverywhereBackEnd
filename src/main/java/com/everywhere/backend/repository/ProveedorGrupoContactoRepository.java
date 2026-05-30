package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.SupplierGroupContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorGrupoContactoRepository extends JpaRepository<SupplierGroupContact, Integer> {

    Optional<SupplierGroupContact> findByNombreIgnoreCase(String nombre);

    List<SupplierGroupContact> findByNombreContainingIgnoreCase(String nombre);
}
