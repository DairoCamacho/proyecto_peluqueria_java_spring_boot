package com.unaux.dairo.api.repository;

import com.unaux.dairo.api.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;


public interface UserRepository extends JpaRepository<User, Integer> {

  UserDetails findByEmail(String username);
  Page<User> findByStatusTrue(Pageable pagination);
  boolean existsByEmail(String email);
  
}
