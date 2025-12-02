package com.healthyPetsBackend.repository;

import com.healthyPetsBackend.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    boolean existsByDueñoIdAndNombreIgnoreCase(Long dueñoId, String nombre);
}
