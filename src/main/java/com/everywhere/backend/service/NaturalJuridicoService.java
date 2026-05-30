package com.everywhere.backend.service;

import com.everywhere.backend.model.dto.NaturalJuridicRequestDTO;
import com.everywhere.backend.model.dto.NaturalJuridicResponseDTO;
import com.everywhere.backend.model.dto.NaturalJuridicPatchDTO;

import java.util.List;

public interface NaturalJuridicoService {
    
    List<NaturalJuridicResponseDTO> crearRelaciones(NaturalJuridicRequestDTO naturalJuridicoRequestDTO);
    List<NaturalJuridicResponseDTO> findByPersonaNaturalId(Integer personaNaturalId);
    List<NaturalJuridicResponseDTO> findByPersonaJuridicaId(Integer personaJuridicaId);
    NaturalJuridicResponseDTO findById(Integer id);
    void deleteById(Integer id);
    void deleteByPersonas(Integer personaNaturalId, Integer personaJuridicaId);
    List<NaturalJuridicResponseDTO> findAll();
    List<NaturalJuridicResponseDTO> patchRelacionesPersonaNatural(Integer personaNaturalId, NaturalJuridicPatchDTO naturalJuridicoPatchDTO);
}