package com.healthyPetsBackend.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CitaUpdateDTO {
    // Todos los campos son opcionales para una actualización parcial
    private Long paciente; // Nuevo Dueño/Paciente ID
    private Long mascota_id; // Nueva Mascota ID
    private Long veterinario_id; // Nuevo Veterinario ID

    private LocalDate fecha; // Nueva fecha de la cita
    private LocalTime hora; // Nueva hora de la cita

    private String motivo; // Nuevo motivo
}
