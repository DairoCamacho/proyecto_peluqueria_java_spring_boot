package com.unaux.dairo.api.repository;

import com.unaux.dairo.api.domain.hairsalon.HairSalon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HairSalonRepository extends JpaRepository<HairSalon, Integer> {

  boolean existsByName(String hairSalonName);
    
}
