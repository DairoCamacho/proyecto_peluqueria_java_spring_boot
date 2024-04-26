package com.unaux.dairo.api.repository;

import com.unaux.dairo.api.domain.workinghours.WorkingHours;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkingHoursRepository
  extends JpaRepository<WorkingHours, String> {}
