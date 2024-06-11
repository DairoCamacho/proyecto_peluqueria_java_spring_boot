package com.unaux.dairo.api.repository;

import com.unaux.dairo.api.domain.appointment.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository
    extends JpaRepository<Appointment, Integer> {
}
