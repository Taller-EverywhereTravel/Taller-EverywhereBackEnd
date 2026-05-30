package com.everywhere.backend.model.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class PersonQuotationId implements Serializable {
	private Integer person; // Cambiar nombre y tipo para que coincida con PersonaCotizacion
	private Integer quotation; // Cambiar tipo a Integer
}