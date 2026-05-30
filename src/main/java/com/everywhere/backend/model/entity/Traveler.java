package com.everywhere.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference; 
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Entity
@Table(name = "viajeros")
public class Traveler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "via_id_int")
    private Integer id;

    @Column(name = "via_fec_nac_tmp")
    private LocalDate dateBirth;

    @Column(name = "via_nacio_vac")
    private String nationality;

    @Column(name = "via_resi_vac")
    private String residence;

    @CreationTimestamp
    @Column(name = "via_cre_tmp", updatable = false)
    private LocalDateTime created;

    @UpdateTimestamp
    @Column(name = "via_upd_tmp")
    private LocalDateTime updated;

    @OneToOne(mappedBy = "viajero", fetch = FetchType.LAZY)
    @JsonBackReference("viajero-personaNatural")
    private PersonNatural personNatural;
}