package com.unaux.dairo.api.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.unaux.dairo.api.domain.workinghours.WorkingHours;

@Repository
public interface WorkingHoursRepository extends JpaRepository<WorkingHours, Integer> {
  @Query("SELECT CASE WHEN EXISTS(" +
        "SELECT 1 FROM WorkingHours wh " +
        "WHERE wh.employee.id = :employeeId AND " +
        "((wh.startDate <= :startDate AND wh.endDate >= :endDate) " +
        "OR (wh.startDate >= :startDate AND wh.startDate <= :endDate) " +
        "OR (wh.endDate >= :startDate AND wh.endDate <= :endDate))" +
        ") THEN TRUE ELSE FALSE END")
  boolean existsByEmployeeIdAndOverlappingDate(int employeeId, LocalDateTime startDate, LocalDateTime endDate);
  }
