package com.everywhere.backend.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@Table(name = "carpetas")
public class Folder {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name="carp_id_int" )
    private int id;

    @Column(name="carp_nom_vac")
    private String name;

    @Column (name = "carp_desc_vac")
    @JdbcTypeCode(SqlTypes.LONGNVARCHAR)
    private String description;

    @CreationTimestamp
    @Column(name="carp_cre_tmp" )
    private LocalDateTime created;

    @UpdateTimestamp
    @Column(name="carp_upd_tmp" )
    private LocalDateTime updated;

    @Column(name="carp_niv_int" )
    private int level;

    @ManyToOne
    @JoinColumn(name = "car_id_padr_int")
    private Folder folderFather;
}