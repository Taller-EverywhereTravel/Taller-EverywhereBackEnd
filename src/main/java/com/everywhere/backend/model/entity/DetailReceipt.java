package com.everywhere.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "detalle_recibo")
@Data
public class DetailReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "det_recibo_id_int")
    private Integer id;

    @Column(name = "det_recibo_cant_int")
    private Integer amount;

    @Column(name = "det_recibo_desc_vac")
    @JdbcTypeCode(SqlTypes.LONGNVARCHAR)
    private String description;

    @Column(name = "det_recibo_prec_dc")
    private BigDecimal price;

    @CreationTimestamp
    @Column(name = "det_recibo_cre_tmp")
    private LocalDateTime dateCreated;

    @UpdateTimestamp
    @Column(name = "det_recibo_upd_tmp")
    private LocalDateTime dateUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recibo_id_int")
    @JsonBackReference
    private Receipt receipt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prod_id_int")
    private Product product;
}