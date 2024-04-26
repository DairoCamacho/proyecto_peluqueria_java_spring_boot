package com.unaux.dairo.api.controller;

import com.unaux.dairo.api.domain.client.Client;
import com.unaux.dairo.api.domain.employee.*;
import com.unaux.dairo.api.domain.hairsalon.HairSalon;
import com.unaux.dairo.api.repository.ClientRepository;
import com.unaux.dairo.api.repository.EmployeeRepository;
import com.unaux.dairo.api.repository.HairSalonRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("api/employee")
public class EmployeeController {

  private final EmployeeRepository employeeRepository;
  private final ClientRepository clientRepository;
  private final HairSalonRepository hairSalonRepository;

  EmployeeController(
    EmployeeRepository employeeRepository,
    ClientRepository clientRepository,
    HairSalonRepository hairSalonRepository
  ) {
    this.employeeRepository = employeeRepository;
    this.clientRepository = clientRepository;
    this.hairSalonRepository = hairSalonRepository;
  }

  @PostMapping
  public ResponseEntity createEmployee(
    @RequestBody @Valid EmployeeCreateDto employeeCreateDto,
    UriComponentsBuilder uriComponentsBuilder
  ) {
    Client client = clientRepository
      .findById(employeeCreateDto.id())
      .orElseThrow(() -> new RuntimeException("User not found"));

    HairSalon hairSalon = hairSalonRepository
      .findById(employeeCreateDto.hairSalonId())
      .orElseThrow(() -> new RuntimeException("Hair salon not found"));

    Employee employee = employeeRepository.save(
      new Employee(employeeCreateDto, client, hairSalon)
    );

    // Creamos un Dto para retornar el objeto creado al frontend
    EmployeeResponseDto response = new EmployeeResponseDto(
      employee.getId(),
      employee.getClient().getId(),
      employee.getClient().getName(),
      employee.getClient().getLastName(),
      employee.getClient().getPhone(),
      employee.getClient().getBirthday(),
      employee.getClient().getUser().getEmail(),
      employee.getPosition(),
      employee.getHireDate(),
      employee.getTerminationDate(),
      employee.getHairSalon().getId()
    );

    URI url = uriComponentsBuilder
      .path("api/employee/{id}")
      .buildAndExpand(employee.getClient().getId())
      .toUri();

    return ResponseEntity.created(url).body(response);
  }

  @GetMapping
  public ResponseEntity<Page<EmployeeFindDto>> listAllEmployee(
    Pageable pagination
  ) {
    return ResponseEntity.ok(
      employeeRepository.findAll(pagination).map(EmployeeFindDto::new)
    );
  }

  @GetMapping("/{id}")
  public ResponseEntity<EmployeeResponseDto> findEmployee(
    @PathVariable int id
  ) {
    Optional<Employee> employeeOptional = employeeRepository.findByClientId(id);
    if (!employeeOptional.isPresent()) {
      return ResponseEntity.notFound().build();
    }

    Employee employee = employeeOptional.get();

    EmployeeResponseDto response = new EmployeeResponseDto(
      employee.getId(),
      employee.getClient().getId(),
      employee.getClient().getName(),
      employee.getClient().getLastName(),
      employee.getClient().getPhone(),
      employee.getClient().getBirthday(),
      employee.getClient().getUser().getEmail(),
      employee.getPosition(),
      employee.getHireDate(),
      employee.getTerminationDate(),
      employee.getHairSalon().getId()
    );

    return ResponseEntity.ok(response);
  }

  @PutMapping
  @Transactional
  public ResponseEntity<EmployeeResponseDto> updateEmployee(
    @RequestBody @Valid EmployeeUpdateDto employeeUpdateDto
  ) {

    // Employee employee = employeeRepository
    //   .findById(employeeUpdateDto.id())
    //   .orElseThrow(() -> new RuntimeException("Employee not found"));

    Employee employee = employeeRepository.getReferenceById(
      employeeUpdateDto.id()
    );

    HairSalon hairSalon = hairSalonRepository.getReferenceById(employee.getHairSalon().getId());
    

    employee.update(employeeUpdateDto, hairSalon);

    EmployeeResponseDto response = new EmployeeResponseDto(
      employee.getId(),
      employee.getClient().getId(),
      employee.getClient().getName(),
      employee.getClient().getLastName(),
      employee.getClient().getPhone(),
      employee.getClient().getBirthday(),
      employee.getClient().getUser().getEmail(),
      employee.getPosition(),
      employee.getHireDate(),
      employee.getTerminationDate(),
      employee.getHairSalon().getId()
    );

    return ResponseEntity.ok(response);
  }

  // CONSULTA POR ID DE EMPLOYEE
  @DeleteMapping("/{id}")
  @Transactional
  public ResponseEntity<Employee> terminationDate(@PathVariable int id) {
    Optional<Employee> employeeOptional = employeeRepository.findByClientId(id);
    if (!employeeOptional.isPresent()) {
      return ResponseEntity.notFound().build();
    }
    Employee employee = employeeOptional.get();
    employee.setTerminationDate(LocalDate.now());
    return ResponseEntity.ok().build();
  }
  /*
  // CONSULTA POR ID DE EMPLOYEE
     @DeleteMapping("/{id}")
  @Transactional
  public ResponseEntity terminationDate(@PathVariable int id) {
    Employee employee = employeeRepository.getReferenceById(id);
    employee.setTerminationDate(LocalDate.now());
    return ResponseEntity.ok().build();
  }
   */
}
