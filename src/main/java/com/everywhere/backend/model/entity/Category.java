
package com.everywhere.backend.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Entity
@Table(name = "categorias")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cat_id_int")
	private int id;

	@Column(name = "cat_nom_vac", length = 100)
	private String name;

	@CreationTimestamp
	@Column(name = "cat_cre_tmp")
	private LocalDateTime created;

	@UpdateTimestamp
	@Column(name = "cat_upd_tmp")
	private LocalDateTime updated;
}