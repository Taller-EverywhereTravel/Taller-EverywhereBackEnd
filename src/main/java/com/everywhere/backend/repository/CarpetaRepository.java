package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CarpetaRepository extends JpaRepository<Folder, Integer> {

    List<Folder> findByCarpetaPadreId(Integer carpetaPadreId);
    List<Folder> findByNivel(Integer nivel);
    List<Folder> findByNombreContainingIgnoreCase(String nombre);
    List<Folder> findByCreadoBetween(LocalDateTime inicio, LocalDateTime fin);
    List<Folder> findByCreadoBetweenOrderByCreadoAsc(LocalDateTime inicio, LocalDateTime fin);
    List<Folder> findByCarpetaPadreIsNull(); // Carpeta raíz (sin padre) 
    List<Folder> findAllByOrderByCreadoDesc(); 

    @Query("SELECT COUNT(c) > 0 FROM Carpeta c WHERE c.nombre = :nombre AND c.nivel = :nivel")
    boolean existsByNombreAndNivel(@Param("nombre") String nombre, @Param("nivel") Integer nivel);

    @Query("SELECT c FROM Carpeta c WHERE YEAR(c.creado) = :anio AND MONTH(c.creado) = :mes")
    List<Folder> findByAnioAndMes(@Param("anio") int anio, @Param("mes") int mes);
}