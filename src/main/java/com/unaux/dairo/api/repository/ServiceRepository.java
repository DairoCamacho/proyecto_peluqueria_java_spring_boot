package com.unaux.dairo.api.repository;

import com.unaux.dairo.api.domain.service.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Integer> {}
