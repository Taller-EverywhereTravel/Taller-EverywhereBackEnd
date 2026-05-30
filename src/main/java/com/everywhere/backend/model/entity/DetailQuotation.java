package com.everywhere.backend.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@Table(name = "detalle_cotizacion")
public class DetailQuotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dcot_id_int")
    private int id;

    @Column(name = "dcot_cant_int")
    private Integer quantity;

    @Column(name = "dcot_und_int")
    private Integer unit;

    @Column(name = "dcot_desc_vac")
    @JdbcTypeCode(SqlTypes.LONGNVARCHAR)
    private String description;

    @Column(name = "dcot_comision_dc")
    private BigDecimal commission;

    @Column(name = "dcot_prec_hist_dc")
    private BigDecimal priceHistory;

    @Column(name = "dcot_select_bol")
    private Boolean selected;

    @CreationTimestamp
    @Column(name = "dcot_cre_tmp", updatable = false)
    private LocalDateTime created;

    @UpdateTimestamp
    @Column(name = "dcot_upd_tmp")
    private LocalDateTime updated;

    @ManyToOne
    @JoinColumn(name = "cat_id_int")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "cot_id_int", nullable = false)
    private Quotation quotation;

    @ManyToOne
    @JoinColumn(name = "prod_id_int")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "prov_id_int")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "opr_id_int", nullable = true)
    private Operator operator;
}