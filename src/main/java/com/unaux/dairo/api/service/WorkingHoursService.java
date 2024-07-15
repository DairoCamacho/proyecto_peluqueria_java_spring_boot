package com.unaux.dairo.api.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.unaux.dairo.api.domain.employee.Employee;
import com.unaux.dairo.api.domain.workinghours.WorkingHours;
import com.unaux.dairo.api.infra.errors.DuplicateWorkingHoursException;
import com.unaux.dairo.api.infra.errors.ResourceNotFoundException;
import com.unaux.dairo.api.repository.EmployeeRepository;
import com.unaux.dairo.api.repository.WorkingHoursRepository;

@Service
public class WorkingHoursService {

  private final WorkingHoursRepository workingHoursRepository;
  private final EmployeeRepository employeeService;

  public WorkingHoursService(WorkingHoursRepository workingHoursRepository, EmployeeRepository employeeService) {
    this.workingHoursRepository = workingHoursRepository;
    this.employeeService = employeeService;
  }

  public WorkingHours save(LocalDateTime startDate, LocalDateTime endDate, int employeeId) {
    // Verificar si existe el empleado existe
    Employee employee = employeeService.findById(employeeId)
        .orElseThrow(
            () -> new ResourceNotFoundException(
                "Employee not found with the ID: " + employeeId));

    // Verificar si existe un registro duplicado o superpuesto
    boolean existsDuplicate = workingHoursRepository.existsByEmployeeIdAndOverlappingDate(employee.getId(), startDate, endDate);
    if (existsDuplicate){
      throw new DuplicateWorkingHoursException("Working hours already exist for employee: " + employeeId + ", start date: " + startDate + ", end date: " + endDate);
    }
    // Guardar el nuevo registro
    return workingHoursRepository.save(new WorkingHours(startDate, endDate, employee));
  }

  public Page<WorkingHours> findAll(Pageable pagination) {
    return workingHoursRepository.findAll(pagination);
  }

  public Page<WorkingHours> findEnabled(Pageable pagination) {
    return workingHoursRepository.findByStatusTrue(pagination);
  }

  public Page<WorkingHours> findActive(Pageable pagination) {
    LocalDateTime currentDate = LocalDateTime.now();
    return workingHoursRepository.findEnabledWorkingHoursAfter(currentDate, pagination);
  }

  public Optional<WorkingHours> findById(int id) {
    return workingHoursRepository.findById(id);
  }

  public WorkingHours update(int id, LocalDateTime startDate, LocalDateTime endDate) {

    // con el ID Buscamos la Entidad a actualizar
    WorkingHours workingHours = workingHoursRepository.getReferenceById(id);
    // Actualizamos la Entidad con los datos del DTO
    workingHours.update(startDate, endDate);
    // retornamos la Entidad ya actualizada
    return workingHours;
  }

  public void delete(int id) {
    // con el ID Buscamos la Entidad para eliminar
    WorkingHours workingHours = workingHoursRepository.getReferenceById(id);
    // Desactivamos - borrado lógico
    workingHours.inactivate();
  }

}
