package com.everywhere.backend.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "personas_cotizaciones")
@Data
@IdClass(PersonQuotationId.class)
public class PersonQuotation {

    @Id
    @ManyToOne
    @JoinColumn(name = "per_id_int", nullable = false)
    private Person person;

    @Id
    @ManyToOne
    @JoinColumn(name = "cot_id_int", nullable = false)
    private Quotation quotation;
}