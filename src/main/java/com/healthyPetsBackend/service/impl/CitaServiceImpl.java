package com.healthyPetsBackend.service.impl;

import com.healthyPetsBackend.dto.CitaCreateDTO;
import com.healthyPetsBackend.dto.CitaResponseDTO;
import com.healthyPetsBackend.dto.CitaUpdateDTO;
import com.healthyPetsBackend.model.Cita;
import com.healthyPetsBackend.model.Pet;
import com.healthyPetsBackend.model.User;
import com.healthyPetsBackend.model.Veterinario;
import com.healthyPetsBackend.repository.CitaRepository;
import com.healthyPetsBackend.repository.PetRepository;
import com.healthyPetsBackend.repository.UserRepository;
import com.healthyPetsBackend.repository.VeterinarioRepository;
import com.healthyPetsBackend.service.CitaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CitaServiceImpl implements CitaService {

    private final CitaRepository repository;

    private final UserRepository userRepository; // Repositorio de Dueño/Paciente
    private final PetRepository petRepository; // Repositorio de Mascota
    private final VeterinarioRepository veterinarioRepository;

    public CitaServiceImpl(
            CitaRepository repository,
            UserRepository userRepository,
            PetRepository petRepository,
            VeterinarioRepository veterinarioRepository) {

        this.repository = repository;
        this.userRepository = userRepository;
        this.petRepository = petRepository;
        this.veterinarioRepository = veterinarioRepository;
    }

    @Override
    public CitaResponseDTO create(CitaCreateDTO dto) {

        // --- Lógica de Validación de Disponibilidad (Necesita corrección) ---
        // NOTA: Tu repositorio ya no tiene el campo Long 'veterinarioId',
        // sino el objeto 'veterinario'. Debes corregir este método en el Repository.
        // Por ahora, lo mantenemos con los IDs del DTO para simular la validación:
        if (repository.existsByVeterinarioIdAndFechaAndHora(dto.getVeterinario_id(), dto.getFecha(), dto.getHora())) {
            throw new RuntimeException("El veterinario ya tiene una cita asignada a esa hora.");
        }

        // --- 1. Buscar las entidades relacionadas ---

        // Asumo que 'paciente' en el DTO es el ID del Dueño/User
        User paciente = userRepository.findById(dto.getPaciente())
                .orElseThrow(() -> new RuntimeException("Dueño/Paciente no encontrado con ID: " + dto.getPaciente()));

        Pet mascota = petRepository.findById(dto.getMascota_id())
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con ID: " + dto.getMascota_id()));

        Veterinario veterinario = veterinarioRepository.findById(dto.getVeterinario_id())
                .orElseThrow(
                        () -> new RuntimeException("Veterinario no encontrado con ID: " + dto.getVeterinario_id()));

        // --- 2. Crear y Mapear Entidad Cita ---

        Cita cita = new Cita();
        cita.setPaciente(paciente); // <-- Asignar objeto User
        cita.setMascota(mascota); // <-- Asignar objeto Pet (asumo que renombraste mascota_id a mascota)
        cita.setVeterinario(veterinario); // <-- Asignar objeto Veterinario (asumo que renombraste veterinarioId a
                                          // veterinario)
        cita.setFecha(dto.getFecha());
        cita.setHora(dto.getHora());
        cita.setMotivo(dto.getMotivo());

        return mapToResponse(repository.save(cita));
    }

    @Override
    public List<CitaResponseDTO> getAll() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CitaResponseDTO update(Long id, CitaUpdateDTO dto) {
        // 1. Buscar la cita existente
        Cita existingCita = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + id));

        // 2. Mapeo Manual y Actualización (Solo si el campo viene en el DTO)

        // Actualizar Dueño/Paciente
        if (dto.getPaciente() != null) {
            User nuevoPaciente = userRepository.findById(dto.getPaciente())
                    .orElseThrow(
                            () -> new RuntimeException("Dueño/Paciente no encontrado con ID: " + dto.getPaciente()));
            existingCita.setPaciente(nuevoPaciente);
        }

        // Actualizar Mascota
        if (dto.getMascota_id() != null) {
            Pet nuevaMascota = petRepository.findById(dto.getMascota_id())
                    .orElseThrow(() -> new RuntimeException("Mascota no encontrada con ID: " + dto.getMascota_id()));
            existingCita.setMascota(nuevaMascota);
        }

        // Actualizar Veterinario
        if (dto.getVeterinario_id() != null) {
            Veterinario nuevoVeterinario = veterinarioRepository.findById(dto.getVeterinario_id())
                    .orElseThrow(
                            () -> new RuntimeException("Veterinario no encontrado con ID: " + dto.getVeterinario_id()));
            existingCita.setVeterinario(nuevoVeterinario);
        }

        // Actualizar Fecha, Hora y Motivo
        if (dto.getFecha() != null) {
            existingCita.setFecha(dto.getFecha());
        }
        if (dto.getHora() != null) {
            existingCita.setHora(dto.getHora());
        }
        if (dto.getMotivo() != null) {
            existingCita.setMotivo(dto.getMotivo());
        }

        // 3. Opcional: Revalidar disponibilidad (Si se cambiaron
        // fecha/hora/veterinario)
        // Se recomienda una validación de unicidad aquí, similar a la del método
        // CREATE,
        // pero asegurándose de excluir la cita actual de la validación.

        // 4. Guardar y retornar la cita actualizada
        return mapToResponse(repository.save(existingCita));
    }

    @Override
    public CitaResponseDTO getById(Long id) {
        Cita cita = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        return mapToResponse(cita);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private CitaResponseDTO mapToResponse(Cita cita) {
        CitaResponseDTO dto = new CitaResponseDTO();
        dto.setId(cita.getId());

        // Obtener los IDs de los objetos relacionados
        dto.setPaciente(cita.getPaciente().getId());
        dto.setMascota_id(cita.getMascota().getId());
        dto.setVeterinario_id(cita.getVeterinario().getId());

        dto.setFecha(cita.getFecha());
        dto.setHora(cita.getHora());
        dto.setMotivo(cita.getMotivo());
        return dto;
    }
}
