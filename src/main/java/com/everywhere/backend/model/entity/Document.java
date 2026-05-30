package com.everywhere.backend.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "documentos")
@Data
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_id_int" )
    private Integer id;

    @Column(name = "doc_tipo_vac", nullable = false, unique = true)
    private String type;

    @Column(name = "doc_desc_vac")
    @JdbcTypeCode(SqlTypes.LONGNVARCHAR)
    private String description;

    @Column(name = "doc_est_bln")
    private Boolean status;

    @CreationTimestamp
    @Column(name = "doc_cre_tmp", updatable = false)
    private LocalDateTime created;

    @UpdateTimestamp
    @Column(name = "doc_upd_tmp")
    private LocalDateTime updated;
}