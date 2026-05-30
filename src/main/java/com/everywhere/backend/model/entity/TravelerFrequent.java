package com.everywhere.backend.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="viajeros_frecuente")
@Data
public class TravelerFrequent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="via_frec_id_int")
    private Integer id;

    @Column(name="via_frec_aer_vac")
    private String airline;

    @Column(name="via_frec_cod_vac")
    private String code;

    @CreationTimestamp
    @Column(name="via_cre_tmp", updatable = false)
    private LocalDateTime created;

    @UpdateTimestamp
    @Column(name="via_upd_tmp")
    private LocalDateTime updated;

    @ManyToOne
    @JoinColumn(name="via_id_int")
    private Traveler traveler;
}