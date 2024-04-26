package com.unaux.dairo.api.controller;

import com.unaux.dairo.api.domain.employee.Employee;
import com.unaux.dairo.api.domain.workinghours.*;
import com.unaux.dairo.api.infra.errors.ControllerExceptionHandler.DataErrorValidationDto;
import com.unaux.dairo.api.repository.EmployeeRepository;
import com.unaux.dairo.api.repository.WorkingHoursRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("api/workinghours")
public class WorkingHoursController {

  private final WorkingHoursRepository workingHoursRepository;
  private final EmployeeRepository employeeRepository;

  WorkingHoursController(
    WorkingHoursRepository workingHoursRepository,
    EmployeeRepository employeeRepository
  ) {
    this.workingHoursRepository = workingHoursRepository;
    this.employeeRepository = employeeRepository;
  }

  @PostMapping
  public ResponseEntity createWorkingHours(
    @RequestBody @Valid WorkingHoursCreateDto workingHoursCreateDto,
    UriComponentsBuilder uriComponentsBuilder
  ) {
    Employee employee = employeeRepository
      .findById(workingHoursCreateDto.employeeId())
      .orElseThrow(() -> new RuntimeException("Employee not found"));
      WorkingHours workingHours = null;
    try {
       workingHours = workingHoursRepository.save(new WorkingHours(workingHoursCreateDto, employee));
    } catch (DataAccessException e) {

      // var error = "Error al guardar el registro: " + e.getMostSpecificCause();
      // return ResponseEntity.badRequest().body(error);

      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .header("Error-DataAccessException", "Error al guardar el registro: " + e.getMostSpecificCause())
        .build();

      
    } catch (Exception e) {
      var error = "Error interno del servidor: " + e.getCause();
      return ResponseEntity.badRequest().body(error);

      // return ResponseEntity
      //   .status(HttpStatus.BAD_REQUEST)
      //   .header(
      //     "Error-DataAccessException",
      //     "Error interno del servidor: " + e.getCause()
      //   )
      //   .build();
    }
    WorkingHoursResponseDto response = new WorkingHoursResponseDto(
      workingHours.getId(),
      workingHours.getStartDate(),
      workingHours.getEndDate(),
      workingHours.getEmployee().getId()
    );

    URI url = uriComponentsBuilder
      .path("api/workinghours/{id}")
      .buildAndExpand(workingHours.getId())
      .toUri();
    
    return ResponseEntity.created(url).body(response);
  }

  @GetMapping
  public ResponseEntity<Page<WorkingHoursFindDto>> listAllWorkingHours(
    Pageable pagination
  ) {
    return ResponseEntity.ok(
      workingHoursRepository.findAll(pagination).map(WorkingHoursFindDto::new)
    );
  }

  @GetMapping("/{id}")
  public ResponseEntity<WorkingHoursResponseDto> findWorkingHours(
    @PathVariable String id
  ) {
    Optional<WorkingHours> workingHoursOptional = workingHoursRepository.findById(
      id
    );
    if (!workingHoursOptional.isPresent()) {
      return ResponseEntity.notFound().build();
    }

    WorkingHours workingHours = workingHoursOptional.get();

    WorkingHoursResponseDto response = new WorkingHoursResponseDto(
      workingHours.getId(),
      workingHours.getStartDate(),
      workingHours.getEndDate(),
      workingHours.getEmployee().getId()
    );

    return ResponseEntity.ok(response);
  }

  @PutMapping
  public ResponseEntity<WorkingHoursResponseDto> updateWorkingHours(
    @RequestBody WorkingHoursUpdateDto workingHoursUpdateDto
  ) {
    WorkingHours workingHours = workingHoursRepository
      .findById(workingHoursUpdateDto.id())
      .orElseThrow(() -> new RuntimeException("WorkingHours not found"));

    workingHours.update(workingHoursUpdateDto);

    WorkingHoursResponseDto response = new WorkingHoursResponseDto(
      workingHours.getId(),
      workingHours.getStartDate(),
      workingHours.getEndDate(),
      workingHours.getEmployee().getId()
    );

    return ResponseEntity.ok(response);
  }

  // OJO: delete físico
  @DeleteMapping("/{id}")
  @Transactional
  public void deleteWorkingHours(@PathVariable String id) {
    WorkingHours workingHours = workingHoursRepository
      .findById(id)
      .orElseThrow(() -> new RuntimeException("WorkingHours not found"));
    workingHoursRepository.delete(workingHours);
  }
}
