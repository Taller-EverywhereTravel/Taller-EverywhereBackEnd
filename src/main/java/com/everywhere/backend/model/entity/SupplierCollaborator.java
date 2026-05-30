package com.everywhere.backend.model.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Entity
@Table(name = "proveedor_colaborador")
public class SupplierCollaborator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prov_col_id_int")
    private Integer id;

    @Column(name = "prov_col_cargo_int", length = 100)
    private String position;

    @Column(name = "prov_col_nom_vac", length = 150)
    private String name;

    @Column(name = "prov_col_email_int", length = 150)
    private String mail;

    @Column(name = "prov_col_telf_int", length = 50)
    private String phone;

    @Column(name = "prov_col_cod_pais_vac", length = 10)
    private String codeCountry;

    @Column(name = "pro_col_detalle_vac", length = 500)
    private String detail;

    @CreationTimestamp
    @Column(name = "prov_col_cre_tmp", updatable = false)
    private LocalDateTime created;

    @UpdateTimestamp
    @Column(name = "prov_col_upd_tmp")
    private LocalDateTime updated;

    @ManyToOne
    @JoinColumn(name = "prov_id_int")
    @JsonIgnoreProperties({ "contactos", "colaboradores" })
    private Supplier supplier;
}
