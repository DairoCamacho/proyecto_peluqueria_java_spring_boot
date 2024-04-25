package com.unaux.dairo.api.repository;

import com.unaux.dairo.api.domain.employee.Employee;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
  Page<Employee> findByTerminationDateNull(Pageable pageable);
  Optional<Employee> findByClientId(int clientId);
}
