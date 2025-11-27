package com.healthyPetsBackend.service.impl;

import org.springframework.stereotype.Service;

import com.healthyPetsBackend.dto.PetCreateDTO;
import com.healthyPetsBackend.dto.PetUpdateDTO;
import com.healthyPetsBackend.model.Pet;
import com.healthyPetsBackend.model.User;
import com.healthyPetsBackend.repository.PetRepository;
import com.healthyPetsBackend.repository.UserRepository;
import com.healthyPetsBackend.service.PetService;

import java.util.List;
import java.util.Optional;

@Service
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository; // Necesario para manejar la relación con el dueño (Owner)

    public PetServiceImpl(PetRepository petRepository, UserRepository userRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    // --- C: Create (Crear) ---
    @Override
    public Pet savePet(PetCreateDTO createDTO) {
        // 1. Buscar el Dueño
        User owner = userRepository.findById(createDTO.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Dueño no encontrado con ID: " + createDTO.getOwnerId()));

        // 2. Mapear DTO a Entidad Pet (Mapeo Manual)
        Pet newPet = new Pet();
        newPet.setName(createDTO.getName());
        newPet.setType(createDTO.getType());
        newPet.setBreed(createDTO.getBreed());
        newPet.setAge(createDTO.getAge());
        newPet.setOwner(owner); // Asignar el objeto User completo

        // 3. Guardar y retornar
        return petRepository.save(newPet);
    }

    // --- R: Read (Leer) ---
    @Override
    public Optional<Pet> getPetById(Long id) {
        return petRepository.findById(id);
    }

    @Override
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    // --- U: Update (Actualizar) ---
    @Override
    public Pet updatePet(Long id, PetUpdateDTO updateDTO) {
        Pet existingPet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con ID: " + id));

        // 1. Actualizar campos simples (Usamos el DTO para traer los nuevos valores)
        if (updateDTO.getName() != null) {
            existingPet.setName(updateDTO.getName());
        }
        if (updateDTO.getType() != null) {
            existingPet.setType(updateDTO.getType());
        }
        if (updateDTO.getBreed() != null) {
            existingPet.setBreed(updateDTO.getBreed());
        }
        if (updateDTO.getAge() > 0) { // Asumimos que la edad debe ser mayor a 0 si se actualiza
            existingPet.setAge(updateDTO.getAge());
        }

        // 2. Actualizar Dueño si se proporciona un nuevo ID de dueño
        if (updateDTO.getOwnerId() != null) {
            User newOwner = userRepository.findById(updateDTO.getOwnerId())
                    .orElseThrow(
                            () -> new RuntimeException("Nuevo Dueño no encontrado con ID: " + updateDTO.getOwnerId()));
            existingPet.setOwner(newOwner);
        }

        // 3. Guardar y retornar la entidad actualizada
        return petRepository.save(existingPet);
    }

    // --- D: Delete (Eliminar) ---
    @Override
    public void deletePet(Long id) {
        petRepository.deleteById(id);
    }
}