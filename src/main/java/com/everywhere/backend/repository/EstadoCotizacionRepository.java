package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.StatusQuotation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoCotizacionRepository extends JpaRepository<StatusQuotation,Integer> {
}