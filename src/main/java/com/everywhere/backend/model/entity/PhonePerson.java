package com.everywhere.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="telefonos_personas")
public class PhonePerson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tel_id_int")
    private Integer id;

    @Column(name = "tel_num_vac", length = 20)
    private String number;

    @Column(name = "tel_cod_vac", length = 5)
    private String codeCountry;

    @Column(name = "tel_tipo_vac", length = 15)
    private String type;

    @Column(name = "tel_desc_vac")
    @JdbcTypeCode(SqlTypes.LONGNVARCHAR)
    private String description;

    @CreationTimestamp
    @Column(name = "tel_per_cre_tmp", updatable = false)
    private LocalDateTime created;

    @UpdateTimestamp
    @Column(name = "tel_per_upd_tmp")
    private LocalDateTime updated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "per_id_int")
    @JsonBackReference
    private Person person;

}