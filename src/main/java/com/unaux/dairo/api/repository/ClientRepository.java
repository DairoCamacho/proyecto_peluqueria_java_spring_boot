package com.unaux.dairo.api.repository;

import com.unaux.dairo.api.domain.client.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
// import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
  // Page<Client> findByStatusTrue(Pageable pagination);
  // @Query("SELECT c FROM Client c JOIN c.user u WHERE u.status = true")
  // Page<Client> findClientsWithStatusUser(Pageable pagination);
  Page<Client> findByStatusTrue(Pageable pagination);
}