package com.everywhere.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference; 
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Entity
@Table(name = "persona_natural")
public class PersonNatural {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "per_nat_id_int")
    private Integer id;

    @Column(name = "per_nat_doc_int")
    private String document;

    @Column(name = "per_nat_nomb_vac")
    private String name;

    @Column(name = "per_nat_apell_pat_vac")
    private String surnamePaternal;

    @Column(name = "per_nat_apell_mat_vac")
    private String surnameMaternal;

    @Column(name = "per_nat_sexo_vac")
    private String sex; 

    @CreationTimestamp
    @Column(name = "per_nat_cre_tmp", updatable = false)
    private LocalDateTime created;

    @UpdateTimestamp
    @Column(name = "per_nat_upd_tmp")
    private LocalDateTime updated;

    @OneToOne
    @JoinColumn(name = "per_id_int", nullable = false)
    private Person person;

    @OneToMany(mappedBy = "personaNatural", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NaturalJuridic> relacionesJuridicas = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "via_id_int")
    @JsonManagedReference("viajero-personaNatural")
    private Traveler traveler;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat_per_id_int")
    private CategoryPerson categoryPerson;
}