package com.everywhere.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@Table(name = "cotizaciones")
public class Quotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cot_id_int")
    private int id;

    @Column(name = "cot_nomb_vac")
    private String nameQuotation;

    @Column(name = "cot_num_vac")
    private String codeQuotation;

    @Column(name = "cot_cant_adlt_int")
    private Integer numAdult;

    @Column(name = "cot_cant_dchd_int")
    private Integer numChild;

    @CreationTimestamp
    @Column(name = "cot_fec_emi_tmp", updatable = false)
    private LocalDateTime dateIssue;

    @Column(name = "cot_fec_venc_tmp")
    private LocalDateTime dateExpiration;

    @UpdateTimestamp
    @Column(name = "cot_fec_upd_tmp")
    private LocalDateTime updated;

    @Column(name = "cot_dest_vac")
    private String originDestination;

    @Column(name = "cot_fec_sal_tmp")
    private LocalDate dateDeparture;

    @Column(name = "cot_fec_reg_tmp")
    private LocalDate dateReturn;

    @Column(name = "cot_mon_vac")
    private String currency;

    @Column(name = "cot_obs_vac")
    @JdbcTypeCode(SqlTypes.LONGNVARCHAR)
    private String observation;

    @ManyToOne
    @JoinColumn(name = "cou_id_int")
    private Counter counter;

    @ManyToOne
    @JoinColumn(name = "form_id_int")
    private MethodPayment methodPayment;

    @ManyToOne
    @JoinColumn(name = "est_cot_id_int")
    private StatusQuotation statusQuotation;

    @ManyToOne
    @JoinColumn(name = "suc_id_int")
    private Branch branch;

    @ManyToOne
    @JoinColumn (name = "carp_id_padr_int")
    private Folder folder;

    @ManyToOne
    @JoinColumn(name = "per_id_int")
    private Person person;

    @OneToMany(mappedBy = "quotation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<DetailQuotation> detail;
}