package com.unaux.dairo.api.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

import com.unaux.dairo.api.domain.employee.Employee;
import com.unaux.dairo.api.domain.employee.EmployeeCreateDto;
import com.unaux.dairo.api.domain.employee.EmployeeFindDto;
import com.unaux.dairo.api.domain.employee.EmployeeResponseDto;
import com.unaux.dairo.api.domain.employee.EmployeeUpdateDto;
import com.unaux.dairo.api.infra.errors.ResourceNotFoundException;
import com.unaux.dairo.api.service.EmployeeService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/employee")
// @PreAuthorize("hasRole('ADMIN')")
// @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class EmployeeController {

  private final EmployeeService employeeService;

  EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @PostMapping
  public ResponseEntity<?> createEmployee(UriComponentsBuilder uriComponentsBuilder,
      @RequestBody @Valid EmployeeCreateDto employeeCreateDto) {
    // Extraemos los datos
    int clientId = employeeCreateDto.clientId();
    String position = employeeCreateDto.position();
    LocalDate hireDate = employeeCreateDto.hireDate();
    int hairSalonId = employeeCreateDto.hairSalonId();
    Set<Integer> products = employeeCreateDto.productsId();

    try {
      Employee employee = employeeService.save(clientId, position, hireDate, hairSalonId, products);
      // Creamos un Dto para retornar el objeto creado al frontend
      EmployeeResponseDto response = mapEmployeeToResponseDto(employee);
      // Aquí crearemos una url que corresponde al objeto que se creó en la base de datos.
      URI url = uriComponentsBuilder
          .path("api/employee/{id}")
          .buildAndExpand(employee.getId())
          .toUri();

      return ResponseEntity.created(url).body(response);
    } catch (ResourceNotFoundException e) {
      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .header(e.getClass().getSimpleName(), e.getMessage())
          .body(e.getMessage());
    }
  }

  @GetMapping
  public ResponseEntity<Page<EmployeeFindDto>> listAllEmployee(Pageable pagination) {
    Page<Employee> listEmployees = employeeService.findAll(pagination);
    return ResponseEntity.ok(listEmployees.map(EmployeeFindDto::new));
  }

  @GetMapping("/enabled")
  public ResponseEntity<Page<EmployeeFindDto>> listEnabledStatusEmployee(Pageable pagination) {
    Page<Employee> listEmployees = employeeService.findEnabled(pagination);
    return ResponseEntity.ok(listEmployees.map(EmployeeFindDto::new));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> findEmployee(@PathVariable int id) {
    // *** No hay validaciones menores para realizar
    Optional<Employee> employeeOptional = employeeService.findById(id);

    if (employeeOptional.isEmpty()) {
      Map<String, Object> errorDetails = new HashMap<>();
      errorDetails.put("code", "RESOURCE_NOT_FOUND");
      errorDetails.put("message", "The requested resource was not found");
      errorDetails.put("details", "No record with the ID " + id + " was found in the database.");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);

      // Enviar un código de estado HTTP 404 Not Found con un mensaje en el cuerpo
      //! return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The requested resource was not found");
    }
    // Creamos un DTO para retornar el objeto al frontend
    Employee employee = employeeOptional.get();
    EmployeeResponseDto response = mapEmployeeToResponseDto(employee);

    return ResponseEntity.ok(response);
  }

  @PutMapping
  @Transactional
  public ResponseEntity<?> updateEmployee(
      @RequestBody @Valid EmployeeUpdateDto employeeUpdateDto) {
    // Extraemos los datos
    int id = employeeUpdateDto.id();
    LocalDate hireDate = employeeUpdateDto.hireDate();
    String position = employeeUpdateDto.position();
    Boolean status = employeeUpdateDto.status();
    LocalDate terminationDate = employeeUpdateDto.terminationDate();
    int hairSalonId = employeeUpdateDto.hairSalonId();
    Set<Integer> productsId = employeeUpdateDto.productsId();

    try {
      Employee employee = employeeService.update(id, hireDate, position, status, terminationDate, hairSalonId, productsId);
      // creamos el DTO para la respuesta
      EmployeeResponseDto response = mapEmployeeToResponseDto(employee);

      return ResponseEntity.ok(response);
    } catch (EntityNotFoundException e) {
      String errorMessage = "Resource not found with ID: %d".formatted(id);
      return ResponseEntity.badRequest().body(errorMessage);
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  @Transactional
  public ResponseEntity<?> terminationDate(@PathVariable int id) {
    try {
      employeeService.delete(id);
      // Retornamos una respuesta vacía
      return ResponseEntity.noContent().build();
    } catch (EntityNotFoundException e) {
      String errorMessage = "Resource not found with ID: %d".formatted(id);
      return ResponseEntity.badRequest().body(errorMessage);
    }
  }

  private EmployeeResponseDto mapEmployeeToResponseDto(Employee employee) {
    return new EmployeeResponseDto(
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
        employee.getHairSalon().getId(),
        employee.isStatus());
  }
}
