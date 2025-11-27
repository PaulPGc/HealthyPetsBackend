package com.healthyPetsBackend.dto;

import lombok.Data;

@Data
public class PetResponseDTO {

    private Long id;
    private String name;
    private String type;
    private String breed;
    private int age;

    // Podemos incluir una versión simplificada del dueño para evitar ciclos y
    // exponer datos sensibles
    private OwnerResponseDTO owner; // Usar un DTO anidado para el dueño

    // DTO anidado simple para el dueño
    @Data
    public static class OwnerResponseDTO {
        private Long id;
        private String fullName; // Mostrar solo el nombre del dueño
    }
}