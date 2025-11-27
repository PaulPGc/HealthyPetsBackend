package com.healthyPetsBackend.service;

import com.healthyPetsBackend.model.ServiceEntity;

import java.util.List;

public interface ServiceService {

    ServiceEntity save(ServiceEntity service);

    List<ServiceEntity> listAll();

    public ServiceEntity update(Long id, ServiceEntity serviceDetails);

    void delete(Long id);

    ServiceEntity getById(Long id);
}
