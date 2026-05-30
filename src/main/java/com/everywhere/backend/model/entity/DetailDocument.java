package com.everywhere.backend.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Entity
@Table(name= "detalle_documento")
public class DetailDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dtdoc_id_int")
    private Integer id;

    @Column(name = "dtdoc_numero_vac")
    private String number;

    @Column(name = "dtdoc_fec_emi_tmp")
    private LocalDate dateIssue;

    @Column(name = "dtdoc_fec_ven_tmp")
    private LocalDate dateExpiration;

    @CreationTimestamp
    @Column(name = "dtdoc_cre_tmp")
    private LocalDateTime created;

    @UpdateTimestamp
    @Column(name = "dtdoc_upd_tmp")
    private LocalDateTime updated;

    @Column(name = "dtdoc_ori_vac")
    private String origin;

    @ManyToOne
    @JoinColumn(name = "doc_id_int")
    private Document document;

    @ManyToOne
    @JoinColumn(name = "per_nat_id_int")
    private PersonNatural personNatural;
}