package com.healthyPetsBackend.dto;

import lombok.Data;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class PetCreateDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El tipo es obligatorio (perro, gato, etc.)")
    private String type;

    private String breed;

    @Min(value = 0, message = "La edad no puede ser negativa")
    private int age;

    @NotNull(message = "El ID del dueño es obligatorio")
    private Long ownerId; // Se usa 'ownerId' en lugar del objeto User completo
}