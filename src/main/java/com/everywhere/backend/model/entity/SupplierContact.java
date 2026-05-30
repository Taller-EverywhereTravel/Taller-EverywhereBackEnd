package com.everywhere.backend.model.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Entity
@Table(name = "proveedor_contacto")
public class SupplierContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prov_cont_id_int")
    private Integer id;

    @Column(name = "prov_cont_desc_int", length = 300)
    private String description;

    @Column(name = "prov_cont_email_vac", length = 150)
    private String mail;

    @Column(name = "prov_cont_numero_vac", length = 50)
    private String number;

    @Column(name = "prov_cont_cod_pais_vac", length = 5)
    private String codeCountry;

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

    @ManyToOne
    @JoinColumn(name = "prov_grup_id_int")
    @JsonIgnoreProperties("contactos")
    private SupplierGroupContact groupContact;
}
