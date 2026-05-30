package com.everywhere.backend.model.dto;

import lombok.Data;
import java.util.List;

@Data
public class NaturalJuridicPatchDTO {
    private List<Integer> add; // IDs de personas jurídicas a agregar
    private List<Integer> remove; // IDs de personas jurídicas a eliminar
}