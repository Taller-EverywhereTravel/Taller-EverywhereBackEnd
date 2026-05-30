package com.everywhere.backend.service;

import com.everywhere.backend.model.dto.PersonNaturalRequestDTO;
import com.everywhere.backend.model.dto.PersonNaturalResponseDTO;

import java.util.List; 

public interface PersonaNaturalService {
    List<PersonNaturalResponseDTO> findAll();
    PersonNaturalResponseDTO findById(Integer id);
    List<PersonNaturalResponseDTO> findByDocumento(String documento);
    List<PersonNaturalResponseDTO> findByNombres(String nombres);
    List<PersonNaturalResponseDTO> findByApellidosPaternos(String apellidosPaternos);
    List<PersonNaturalResponseDTO> findByApellidosMaternos(String apellidosMaternos);
    PersonNaturalResponseDTO save(PersonNaturalRequestDTO personaNaturalRequestDTO); 
    PersonNaturalResponseDTO patch(Integer id, PersonNaturalRequestDTO personaNaturalRequestDTO);
    void deleteById(Integer id);
    PersonNaturalResponseDTO asociarViajero(Integer personaNaturalId, Integer viajeroId);
    PersonNaturalResponseDTO desasociarViajero(Integer personaNaturalId);
}