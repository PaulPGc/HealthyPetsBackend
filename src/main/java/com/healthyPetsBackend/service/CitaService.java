package com.healthyPetsBackend.service;

import com.healthyPetsBackend.dto.CitaCreateDTO;
import com.healthyPetsBackend.dto.CitaResponseDTO;
import com.healthyPetsBackend.dto.CitaUpdateDTO;

import java.util.List;

public interface CitaService {

    CitaResponseDTO create(CitaCreateDTO dto);

    List<CitaResponseDTO> getAll();

    CitaResponseDTO update(Long id, CitaUpdateDTO dto);

    CitaResponseDTO getById(Long id);

    void delete(Long id);
}
