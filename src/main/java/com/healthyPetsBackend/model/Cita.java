package com.healthyPetsBackend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Table(
    name = "citas",
    uniqueConstraints = @UniqueConstraint(columnNames = {"paciente", "fecha", "hora"})
)
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paciente_id")
    private Long paciente;       // id del paciente (mascota)

    private LocalDate fecha;
    private LocalTime hora;

    private String motivo;
}
