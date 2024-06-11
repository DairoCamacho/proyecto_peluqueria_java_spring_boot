package com.unaux.dairo.api.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

import com.unaux.dairo.api.domain.workinghours.WorkingHours;
import com.unaux.dairo.api.domain.workinghours.WorkingHoursCreateDto;
import com.unaux.dairo.api.domain.workinghours.WorkingHoursFindDto;
import com.unaux.dairo.api.domain.workinghours.WorkingHoursResponseDto;
import com.unaux.dairo.api.domain.workinghours.WorkingHoursUpdateDto;
import com.unaux.dairo.api.infra.errors.ResourceNotFoundException;
import com.unaux.dairo.api.service.WorkingHoursService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/workinghours")
// @PreAuthorize("hasRole('ADMIN')")
// @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class WorkingHoursController {

  private final WorkingHoursService workingHoursService;

  public WorkingHoursController(WorkingHoursService workingHoursService) {
    this.workingHoursService = workingHoursService;
  }

  @PostMapping
  public ResponseEntity<?> createWorkingHours(UriComponentsBuilder uriComponentsBuilder,
      @RequestBody @Valid WorkingHoursCreateDto workingHoursCreateDto) {
    // Extraer los Datos
    LocalDateTime startDate = workingHoursCreateDto.startDate();
    LocalDateTime endDate = workingHoursCreateDto.endDate();
    int employeeId = workingHoursCreateDto.employeeId();

    try {
      WorkingHours workingHours = workingHoursService.save(startDate, endDate, employeeId);
      // Creamos un Dto para retornar el objeto creado al frontend
      WorkingHoursResponseDto response = mapWorkingHoursToResponseDto(workingHours);
      // Aquí crearemos una url que corresponde al objeto que se creó en la base de datos.
      URI url = uriComponentsBuilder
          .path("api/workinghours/{id}")
          .buildAndExpand(workingHours.getId())
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
  public ResponseEntity<Page<WorkingHoursFindDto>> listAllWorkingHours(Pageable pagination) {
    Page<WorkingHours> listWorkingHours = workingHoursService.findAll(pagination);
    return ResponseEntity.ok(listWorkingHours.map(WorkingHoursFindDto::new));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> findWorkingHours(@PathVariable int id) {
    // *** No hay validaciones menores para realizar
    Optional<WorkingHours> workingHoursOptional = workingHoursService.findById(id);

    if (!workingHoursOptional.isPresent()) {

      Map<String, Object> errorDetails = new HashMap<>();
      errorDetails.put("code", "RESOURCE_NOT_FOUND");
      errorDetails.put("message", "The requested resource was not found");
      errorDetails.put("details", "No record with the ID " + id + " was found in the database.");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);

      // Enviar un código de estado HTTP 404 Not Found con un mensaje en el cuerpo
      //! return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The requested resource was not found");
    }
    // Creamos un DTO para retornar el objeto al frontend
    WorkingHours workingHours = workingHoursOptional.get();
    WorkingHoursResponseDto response = mapWorkingHoursToResponseDto(workingHours);

    return ResponseEntity.ok(response);
  }

  @PutMapping
  @Transactional
  public ResponseEntity<?> updateWorkingHours(@RequestBody WorkingHoursUpdateDto workingHoursUpdateDto) {
    // Extraer los datos
    int id = workingHoursUpdateDto.id();
    LocalDateTime startDate = workingHoursUpdateDto.startDate();
    LocalDateTime endDate = workingHoursUpdateDto.endDate();

    try {
      WorkingHours workingHours = workingHoursService.update(id, startDate, endDate);
      // creamos el DTO para la respuesta
      WorkingHoursResponseDto response = mapWorkingHoursToResponseDto(workingHours);

      return ResponseEntity.ok(response);
    } catch (EntityNotFoundException e) {
      String errorMessage = String.format("Resource not found with ID: %d", id);
      return ResponseEntity.badRequest().body(errorMessage);
    }
  }

  @DeleteMapping("/{id}")
  @Transactional
  public ResponseEntity<?> deleteWorkingHours(@PathVariable int id) {
    try {
      workingHoursService.delete(id);
      // Retornamos una respuesta vacía
      return ResponseEntity.noContent().build();
    } catch (EntityNotFoundException e) {
      String errorMessage = String.format("Resource not found with ID: %d", id);
      return ResponseEntity.badRequest().body(errorMessage);
    }
  }

  private WorkingHoursResponseDto mapWorkingHoursToResponseDto(WorkingHours workingHours) {
    return new WorkingHoursResponseDto(
        workingHours.getId(),
        workingHours.getStartDate(),
        workingHours.getEndDate(),
        workingHours.getEmployee().getId(),
        workingHours.isStatus());
  }
}
