package com.healthyPetsBackend.service.impl;

import com.healthyPetsBackend.dto.PacienteCreateDTO;
import com.healthyPetsBackend.dto.PacienteResponseDTO;
import com.healthyPetsBackend.dto.PacienteUpdateDTO;
import com.healthyPetsBackend.dto.UserResponseDTO;
import com.healthyPetsBackend.model.Paciente;
import com.healthyPetsBackend.model.User;
import com.healthyPetsBackend.repository.PacienteRepository;
import com.healthyPetsBackend.service.PacienteService;
import com.healthyPetsBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteServiceImpl implements PacienteService {

    @Autowired
    private PacienteRepository repo;

    @Autowired
    private UserService userService;

    @Override
    public PacienteResponseDTO create(PacienteCreateDTO dto) {

        if (repo.existsByDueñoIdAndNombreIgnoreCase(dto.getDuenoId(), dto.getNombre())) {
            throw new RuntimeException("Ya existe un paciente con ese nombre para este dueño");
        }

        UserResponseDTO duenoDTO = userService.getById(dto.getDuenoId());
        if (duenoDTO == null)
            throw new RuntimeException("Dueño no encontrado");

        Paciente p = new Paciente();

        User dueno = new User();
        dueno.setId(duenoDTO.getId());
        dueno.setFullName(duenoDTO.getFullName());

        p.setDueño(dueno);
        p.setNombre(dto.getNombre());
        p.setEspecie(dto.getEspecie());
        p.setRaza(dto.getRaza());
        p.setEdad(dto.getEdad());

        Paciente saved = repo.save(p);
        return mapToDTO(saved);
    }

    @Override
    public List<PacienteResponseDTO> getAll() {
        return repo.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public PacienteResponseDTO getById(Long id) {
        Paciente p = findEntityById(id);
        return mapToDTO(p);
    }

    @Override
    public PacienteResponseDTO update(Long id, PacienteUpdateDTO dto) {

        Paciente p = findEntityById(id);

        if (repo.existsByDueñoIdAndNombreIgnoreCase(dto.getDuenoId(), dto.getNombre())
                && !p.getNombre().equalsIgnoreCase(dto.getNombre())) {

            throw new RuntimeException("Ya existe un paciente con ese nombre para este dueño");
        }

        UserResponseDTO duenoDTO = userService.getById(dto.getDuenoId());
        if (duenoDTO == null)
            throw new RuntimeException("Dueño no encontrado");

        User dueno = new User();
        dueno.setId(duenoDTO.getId());
        dueno.setFullName(duenoDTO.getFullName());

        p.setDueño(dueno);
        p.setNombre(dto.getNombre());
        p.setEspecie(dto.getEspecie());
        p.setRaza(dto.getRaza());
        p.setEdad(dto.getEdad());

        Paciente updated = repo.save(p);
        return mapToDTO(updated);
    }


    @Override
    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new RuntimeException("Paciente no encontrado");

        repo.deleteById(id);
    }

    @Override
    public Paciente findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public Paciente findEntityById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
    }

    private PacienteResponseDTO mapToDTO(Paciente p) {
        PacienteResponseDTO dto = new PacienteResponseDTO();
        dto.setId(p.getId());
        // p.getDueño() existe porque tu entidad usa 'dueño'
        dto.setDuenoId(p.getDueño() != null ? p.getDueño().getId() : null);
        dto.setDuenoNombre(p.getDueño() != null ? p.getDueño().getFullName() : null);
        dto.setNombre(p.getNombre());
        dto.setEspecie(p.getEspecie());
        dto.setRaza(p.getRaza());
        dto.setEdad(p.getEdad());
        return dto;
    }
}
