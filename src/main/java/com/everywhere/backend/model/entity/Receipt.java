package com.everywhere.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "recibo")
@Data
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recibo_id_int")
    private Integer id;

    @Column(name = "recibo_serie_vac")
    private String serie;

    @Column(name = "recibo_corre_vac")
    private Integer correlative;

    @Column(name = "recibo_fec_emi_tmp")
    private LocalDate dateIssue;

    @Column(name = "recibo_obs_vac")
    @JdbcTypeCode(SqlTypes.LONGNVARCHAR)
    private String observation;

    @Column(name = "recibo_fec_ven_tmp")
    private LocalDate dateExpiration;

    @Column(name = "recibo_file_ven_vac")
    private String fileVenta;

    @Column(name = "recibo_mon_vac")
    private String currency;

    @CreationTimestamp
    @Column(name = "recibo_created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "recibo_updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "cot_id_int", unique = true)
    private Quotation quotation;

    @ManyToOne
    @JoinColumn(name = "carp_id_int")
    private Folder folder;

    @ManyToOne
    @JoinColumn(name = "form_id_int")
    private MethodPayment methodPayment;

    @ManyToOne
    @JoinColumn(name = "usr_id_int")
    private User user;

    @ManyToOne
    @JoinColumn(name = "suc_id_int")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "per_id_int")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "per_jur_id_int")
    private PersonJuridic personJuridic;

    @ManyToOne
    @JoinColumn(name = "dtdoc_id_int")
    private DetailDocument detailDocument;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<DetailReceipt> detailReceipt;

}