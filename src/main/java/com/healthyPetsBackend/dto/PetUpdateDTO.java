package com.healthyPetsBackend.dto;

import lombok.Data;
import jakarta.validation.constraints.Min;

@Data
public class PetUpdateDTO {

    private String name;
    private String type;
    private String breed;

    @Min(value = 0, message = "La edad no puede ser negativa")
    private int age;

    private Long ownerId; // Opcional, solo si se permite cambiar el dueño
}