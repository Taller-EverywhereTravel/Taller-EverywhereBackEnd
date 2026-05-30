package com.everywhere.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailDocumentSearchDto {
    
    // De DetalleDocumento
    private String number;
    
    // De Personas (persona vinculada a PersonaNatural)
    private Integer personId;
    
    // De PersonaNatural
    private String name;
    private String surnamePaternal;
    private String surnameMaternal;
    private String sex;
}
