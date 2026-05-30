package com.everywhere.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailDocumentWithPersonDto {
    
    private String numberDocument;
    private String typeDocument;
    private List<PersonaInfo> person;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PersonaInfo {
        private Integer personId;
        private String nameComplete;
    }
}
