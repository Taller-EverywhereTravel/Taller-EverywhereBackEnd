package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CarpetaRepository extends JpaRepository<Folder, Integer> {

    List<Folder> findByFolderFatherId(Integer carpetaPadreId);
    List<Folder> findByLevel(Integer nivel);
    List<Folder> findByNameContainingIgnoreCase(String nombre);
    List<Folder> findByCreatedBetween(LocalDateTime inicio, LocalDateTime fin);
    List<Folder> findByCreatedBetweenOrderByCreatedAsc(LocalDateTime inicio, LocalDateTime fin);
    List<Folder> findByFolderFatherIsNull(); // Carpeta raíz (sin padre) 
    List<Folder> findAllByOrderByCreatedDesc(); 

    boolean existsByNameAndLevel(String nombre,Integer nivel);

    @Query("SELECT f FROM Folder f WHERE YEAR(f.created) = ?1 AND MONTH(f.created) = ?2")
    List<Folder> findByAnioAndMes(int anio, int mes);
}