package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.SupplierContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorContactoRepository extends JpaRepository<SupplierContact, Integer> {

    List<SupplierContact> findByProveedorId(Integer proveedorId);

    List<SupplierContact> findByGrupoContactoId(Integer grupoContactoId);

    Optional<SupplierContact> findByEmail(String email);
}
