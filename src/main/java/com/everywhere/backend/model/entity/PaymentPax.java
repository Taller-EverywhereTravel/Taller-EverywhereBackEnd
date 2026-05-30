package com.everywhere.backend.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pago_pax")
public class PaymentPax {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pax_id_int")
    private Integer id;

    @Column(name = "pax_monto_dc", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "pax_moned_vac", length = 10)
    private String currency;

    @Column(name = "pax_detalle_vac", length = 500)
    private String detail;

    @CreationTimestamp
    @Column(name = "pax_fec_cre_tmp", updatable = false)
    private LocalDateTime created;

    @UpdateTimestamp
    @Column(name = "pax_fec_upd_tmp")
    private LocalDateTime updated;

    @ManyToOne
    @JoinColumn(name = "liq_id_int")
    private Liquidation liquidation;

    @ManyToOne
    @JoinColumn(name = "form_id_int")
    private MethodPayment methodPayment;
}
