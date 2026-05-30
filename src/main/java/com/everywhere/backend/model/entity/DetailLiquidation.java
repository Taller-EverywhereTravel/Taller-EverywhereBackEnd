package com.everywhere.backend.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@Entity
@Table(name = "detalle_liquidacion")
public class DetailLiquidation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dtliq_id_int")
    private Integer id;

    @Column(name = "dtliq_tick_vac")
    private String ticket;

    @Column(name = "dtliq_doc_cobr_vac")
    private String documentCollection;

    @Column(name = "dtliq_cost_tick_dc")
    private BigDecimal costTicket;

    @Column(name = "dtliq_carg_serv_dc")
    private BigDecimal chargeService;

    @Column(name = "dtliq_val_vent_dc")
    private BigDecimal valueSale;

    @Column(name = "dtliq_fee_emision_vac")
    private String feeEmision;

    @Column(name = "dtliq_doc_fee_vac")
    private String documentFee;

    @Column(name = "dtliq_comision_vac")
    private String commission;

    @Column(name = "dtliq_fac_comp_vac")
    private String invoicePurchase;

    @Column(name = "dtliq_bol_fac_pasj_vac")
    private String ticketPassenger;

    @Column(name = "dtliq_mont_desct_dc")
    private BigDecimal amountDiscount;

    @Column(name = "dtliq_pag_pax_dol_dc")
    private BigDecimal paymentPaxUSD;

    @Column(name = "dtliq_pag_pax_sol_dc")
    private BigDecimal paymentPaxPEN;

    @CreationTimestamp
    @Column(name = "dtliq_fec_cre_tmp", updatable = false)
    private LocalDateTime created;

    @UpdateTimestamp
    @Column(name = "dtliq_fec_upd_tmp")
    private LocalDateTime updated;

    @ManyToOne
    @JoinColumn(name = "opr_id_int", nullable = true)
    private Operator operator;

    @ManyToOne
    @JoinColumn(name = "prov_id_int", nullable = true)
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "via_id_int", nullable = true)
    private Traveler traveler;

    @ManyToOne
    @JoinColumn(name = "prod_id_int", nullable = true)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "liq_id_int", nullable = false)
    private Liquidation liquidation;

}