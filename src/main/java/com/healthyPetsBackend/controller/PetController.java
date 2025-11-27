package com.healthyPetsBackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.healthyPetsBackend.dto.PetCreateDTO;
import com.healthyPetsBackend.dto.PetResponseDTO;
import com.healthyPetsBackend.dto.PetUpdateDTO;
import com.healthyPetsBackend.model.Pet;
import com.healthyPetsBackend.service.PetService;

import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    // 1. CREATE (POST)
    @PostMapping
    public ResponseEntity<PetResponseDTO> createPet(@RequestBody PetCreateDTO createDTO) {
        // El servicio recibe el DTO y retorna la entidad Pet
        Pet savedPet = petService.savePet(createDTO);

        // Mapeo manual de Entidad Pet a Response DTO
        PetResponseDTO responseDTO = mapToResponseDTO(savedPet);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // 2. READ All (GET)
    @GetMapping
    public ResponseEntity<List<Pet>> getAllPets() {
        List<Pet> pets = petService.getAllPets();
        return ResponseEntity.ok(pets); // Código 200
    }

    // 3. READ By ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long id) {
        return petService.getPetById(id)
                .map(ResponseEntity::ok) // Si lo encuentra, devuelve 200 OK
                .orElse(ResponseEntity.notFound().build()); // Si no lo encuentra, devuelve 404 Not Found
    }

    // 4. UPDATE (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<PetResponseDTO> updatePet(@PathVariable Long id, @RequestBody PetUpdateDTO updateDTO) {
        try {
            // El servicio recibe el DTO y retorna la entidad Pet actualizada
            Pet updatedPet = petService.updatePet(id, updateDTO);

            // Mapeo manual de Entidad Pet a Response DTO
            PetResponseDTO responseDTO = mapToResponseDTO(updatedPet);

            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            // Manejar 404 si la mascota o el dueño no existe
            return ResponseEntity.notFound().build();
        }
    }

    // 5. DELETE (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.deletePet(id);
        return ResponseEntity.noContent().build(); // Código 204 No Content
    }

    private PetResponseDTO mapToResponseDTO(Pet pet) {
        PetResponseDTO dto = new PetResponseDTO();
        dto.setId(pet.getId());
        dto.setName(pet.getName());
        dto.setType(pet.getType());
        dto.setBreed(pet.getBreed());
        dto.setAge(pet.getAge());

        if (pet.getOwner() != null) {
            // Mapeo del DTO anidado para el dueño
            PetResponseDTO.OwnerResponseDTO ownerDto = new PetResponseDTO.OwnerResponseDTO();
            ownerDto.setId(pet.getOwner().getId());
            ownerDto.setFullName(pet.getOwner().getFullName());
            dto.setOwner(ownerDto);
        }
        return dto;
    }

}