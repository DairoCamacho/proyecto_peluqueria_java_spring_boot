package com.unaux.dairo.api.repository;


import com.unaux.dairo.api.domain.client.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    Page<Client> findByStatusTrue(Pageable paginacion);
}
