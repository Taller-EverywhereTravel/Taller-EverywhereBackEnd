package com.everywhere.backend.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sucursal")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "suc_id_int")
    private Integer id;

    @Column(name = "suc_desc_vac", length = 200)
    private String description;

    @Column(name = "suc_direc_vac", length = 300)
    private String address;

    @Column(name = "suc_tele_vac")
    private String phone;

    @Column(name = "suc_emai_vac", length = 100)
    private String mail;

    @Column(name = "suc_esta_bool")
    private Boolean status;

    @CreationTimestamp
    @Column(name = "suc_cre_tmp", updatable = false)
    private LocalDateTime dateCreated;

    @UpdateTimestamp
    @Column(name = "suc_act_tmp")
    private LocalDateTime dateUpdated;
}
