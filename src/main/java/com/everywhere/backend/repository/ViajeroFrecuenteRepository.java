package com.everywhere.backend.repository;

import com.everywhere.backend.model.entity.TravelerFrequent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViajeroFrecuenteRepository extends JpaRepository<TravelerFrequent, Integer> {
    List<TravelerFrequent> findByTravelerId(Integer viajeroId);
    boolean existsByAirlineAndCode(String airline, String code);
}