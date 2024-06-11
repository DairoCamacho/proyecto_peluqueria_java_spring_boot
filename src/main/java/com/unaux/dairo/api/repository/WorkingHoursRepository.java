package com.unaux.dairo.api.repository;

import com.unaux.dairo.api.domain.workinghours.WorkingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkingHoursRepository
    extends JpaRepository<WorkingHours, Integer> {
}
