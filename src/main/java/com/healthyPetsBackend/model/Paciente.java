package com.healthyPetsBackend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(
    name = "pacientes",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"dueno_id", "nombre"})
    }
)
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dueno_id")
    private User dueño;

    private String nombre;
    private String especie;
    private String raza;
    private Integer edad;
}


