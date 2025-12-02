package com.healthyPetsBackend.service.impl;

import com.healthyPetsBackend.dto.HorarioCreateDTO;
import com.healthyPetsBackend.dto.HorarioResponseDTO;
import com.healthyPetsBackend.dto.HorarioUpdateDTO;
import com.healthyPetsBackend.dto.VeterinarioResponseDTO;
import com.healthyPetsBackend.model.Horario;
import com.healthyPetsBackend.model.Veterinario;
import com.healthyPetsBackend.repository.HorarioRepository;
import com.healthyPetsBackend.service.HorarioService;
import com.healthyPetsBackend.service.VeterinarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class HorarioServiceImpl implements HorarioService {

    @Autowired
    private HorarioRepository repo;

    @Autowired
    private VeterinarioService veterinarioService;

    private boolean hasOverlap(Long veterinarioId, String dia, LocalTime inicio, LocalTime fin, Long ignoreId) {
        List<Horario> horarios = repo.findByVeterinarioId(veterinarioId);

        return horarios.stream().anyMatch(h ->
                h.getDia().equalsIgnoreCase(dia)
                        && (ignoreId == null || !h.getId().equals(ignoreId))
                        && (

                                (inicio.isAfter(h.getHoraInicio()) && inicio.isBefore(h.getHoraFin())) ||
                                (fin.isAfter(h.getHoraInicio()) && fin.isBefore(h.getHoraFin())) ||
                                (inicio.isBefore(h.getHoraInicio()) && fin.isAfter(h.getHoraFin())) ||
                                (inicio.equals(h.getHoraInicio()) && fin.equals(h.getHoraFin()))
                        )
        );
    }

    @Override
    public HorarioResponseDTO create(HorarioCreateDTO dto) {
        VeterinarioResponseDTO vetDto = veterinarioService.getById(dto.getVeterinarioId());
        if (vetDto == null) throw new RuntimeException("Veterinario no encontrado");

        LocalTime inicio = LocalTime.parse(dto.getHoraInicio());
        LocalTime fin = LocalTime.parse(dto.getHoraFin());

        if (fin.isBefore(inicio) || fin.equals(inicio))
            throw new RuntimeException("La hora fin debe ser mayor a la hora inicio");

        if (hasOverlap(dto.getVeterinarioId(), dto.getDia(), inicio, fin, null)) {
            throw new RuntimeException("Ya existe un horario que se solapa con este");
        }

        Horario h = new Horario();
        Veterinario vet = new Veterinario();
        vet.setId(vetDto.getId());
        vet.setNombre(vetDto.getNombre());

        h.setVeterinario(vet);
        h.setDia(dto.getDia());
        h.setHoraInicio(inicio);
        h.setHoraFin(fin);

        Horario saved = repo.save(h);
        return mapToDTO(saved);
    }

    @Override
    public List<HorarioResponseDTO> getAll() {
        return repo.findAll().stream().map(this::mapToDTO).toList();
    }

    @Override
    public HorarioResponseDTO getById(Long id) {
        Horario h = repo.findById(id).orElseThrow(() -> new RuntimeException("Horario no encontrado"));
        return mapToDTO(h);
    }

    @Override
    public HorarioResponseDTO update(Long id, HorarioUpdateDTO dto) {
        Horario h = repo.findById(id).orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        VeterinarioResponseDTO vetDto = veterinarioService.getById(dto.getVeterinarioId());
        if (vetDto == null) throw new RuntimeException("Veterinario no encontrado");

        LocalTime inicio = LocalTime.parse(dto.getHoraInicio());
        LocalTime fin = LocalTime.parse(dto.getHoraFin());

        if (fin.isBefore(inicio) || fin.equals(inicio))
            throw new RuntimeException("La hora fin debe ser mayor a la hora inicio");

        if (hasOverlap(dto.getVeterinarioId(), dto.getDia(), inicio, fin, id)) {
            throw new RuntimeException("Ya existe un horario que se solapa con este");
        }

        Veterinario vet = new Veterinario();
        vet.setId(vetDto.getId());
        vet.setNombre(vetDto.getNombre());

        h.setVeterinario(vet);
        h.setDia(dto.getDia());
        h.setHoraInicio(inicio);
        h.setHoraFin(fin);

        Horario updated = repo.save(h);
        return mapToDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new RuntimeException("Horario no encontrado");
        repo.deleteById(id);
    }

    @Override
    public Horario findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public boolean isAvailable(Long veterinarioId, String dia, String hora) {
        LocalTime horaCita = LocalTime.parse(hora);

        return repo.findByVeterinarioId(veterinarioId)
                .stream()
                .anyMatch(h ->
                        h.getDia().equalsIgnoreCase(dia)
                                && !horaCita.isBefore(h.getHoraInicio())
                                && !horaCita.isAfter(h.getHoraFin())
                );
    }

    @Override
    public List<Horario> findByVeterinarioId(Long veterinarioId) {
        return repo.findByVeterinarioId(veterinarioId);
    }

    private HorarioResponseDTO mapToDTO(Horario h) {
        HorarioResponseDTO dto = new HorarioResponseDTO();
        dto.setId(h.getId());
        dto.setVeterinarioId(h.getVeterinario().getId());
        dto.setVeterinarioNombre(h.getVeterinario().getNombre());
        dto.setDia(h.getDia());
        dto.setHoraInicio(h.getHoraInicio().toString());
        dto.setHoraFin(h.getHoraFin().toString());
        return dto;
    }
}
