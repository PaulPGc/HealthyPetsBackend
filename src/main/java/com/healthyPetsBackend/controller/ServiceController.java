package com.healthyPetsBackend.controller;

import com.healthyPetsBackend.model.ServiceEntity;
import com.healthyPetsBackend.service.ServiceService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/services")
@CrossOrigin(origins = "*")
public class ServiceController {

    private final ServiceService service;

    public ServiceController(ServiceService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ServiceEntity create(@RequestBody ServiceEntity serviceEntity) {
        return service.save(serviceEntity);
    }

    @GetMapping
    public List<ServiceEntity> list() {
        return service.listAll();
    }

    @PutMapping("/{id}") // Usa PUT y el ID en el path
    public ResponseEntity<ServiceEntity> updateService(
            @PathVariable Long id,
            @RequestBody ServiceEntity serviceDetails) {

        // Llamar al servicio para realizar la actualización
        ServiceEntity updatedService = service.update(id, serviceDetails);

        // Si la actualización fue exitosa, retorna 200 OK con la entidad actualizada
        return ResponseEntity.ok(updatedService);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceEntity> getServiceById(@PathVariable Long id) {
        ServiceEntity found = service.getById(id);
        if (found != null) {
            return ResponseEntity.ok(found);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
