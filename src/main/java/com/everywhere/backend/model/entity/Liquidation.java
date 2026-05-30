package com.everywhere.backend.model.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@Entity
@Table(name = "liquidacion")
public class Liquidation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "liq_id_int")
    private Integer id;

    @Column(name = "liq_num_vac")
    private String number;

    @Column(name = "liq_fec_comp_tmp")
    private LocalDate datePurchase; 

    @Column(name = "liq_dest_vac")
    private String destiny;

    @Column(name = "liq_nro_pasj_int")
    private Integer numberPassenger; 

    @CreationTimestamp
    @Column(name = "liq_fec_cre_tmp", updatable = false)
    private LocalDateTime created;

    @UpdateTimestamp
    @Column(name = "liq_fec_upd_tmp")
    private LocalDateTime updated;

    @OneToOne
    @JoinColumn(name = "cot_id_int")
    private Quotation quotation;

    @ManyToOne
    @JoinColumn(name = "prod_id_int", nullable = true)
    private Product product;

    @OneToMany(mappedBy = "liquidacion")
    private List<ObservationLiquidation> observacionesLiquidacion;

    @ManyToOne
    @JoinColumn(name = "form_id_int", nullable = true)
    private MethodPayment methodPayment;

    @ManyToOne
    @JoinColumn(name = "carp_id_padr_int", nullable = true)
    private Folder folder;
}