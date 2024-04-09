package com.unaux.dairo.api.repository;

import com.unaux.dairo.api.domain.hairsalon.HairSalon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HairSalonRepository extends JpaRepository<HairSalon, Integer> {
    
}
