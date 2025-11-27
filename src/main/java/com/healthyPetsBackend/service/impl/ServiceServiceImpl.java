package com.healthyPetsBackend.service.impl;

import com.healthyPetsBackend.model.ServiceEntity;
import com.healthyPetsBackend.repository.ServiceRepository;
import com.healthyPetsBackend.service.ServiceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository repository;

    public ServiceServiceImpl(ServiceRepository repository) {
        this.repository = repository;
    }

    @Override
    public ServiceEntity save(ServiceEntity service) {
        return repository.save(service);
    }

    @Override
    public List<ServiceEntity> listAll() {
        return repository.findAll();
    }

    @Override
    public ServiceEntity update(Long id, ServiceEntity serviceDetails) {
        // 1. Buscar la entidad existente por ID
        ServiceEntity existingService = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + id));

        // 2. Actualizar los campos
        existingService.setName(serviceDetails.getName());
        existingService.setPrice(serviceDetails.getPrice());
        existingService.setDescription(serviceDetails.getDescription());

        // 3. Guardar y retornar la entidad actualizada
        return repository.save(existingService);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public ServiceEntity getById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
