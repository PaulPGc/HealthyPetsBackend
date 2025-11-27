package com.healthyPetsBackend.service;

import java.util.List;
import java.util.Optional;

import com.healthyPetsBackend.dto.PetCreateDTO;
import com.healthyPetsBackend.dto.PetUpdateDTO;
import com.healthyPetsBackend.model.Pet;

public interface PetService {

    Pet savePet(PetCreateDTO createDTO);

    Optional<Pet> getPetById(Long id);

    List<Pet> getAllPets();

    Pet updatePet(Long id, PetUpdateDTO updateDTO);

    void deletePet(Long id);

}
