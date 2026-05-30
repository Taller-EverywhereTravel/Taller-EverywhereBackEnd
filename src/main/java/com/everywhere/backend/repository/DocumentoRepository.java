package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentoRepository extends JpaRepository<Document, Integer> {
}