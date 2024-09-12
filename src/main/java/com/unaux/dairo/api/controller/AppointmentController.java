package com.unaux.dairo.api.controller;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

import com.unaux.dairo.api.domain.appointment.Appointment;
import com.unaux.dairo.api.domain.appointment.AppointmentCreateDto;
import com.unaux.dairo.api.domain.appointment.AppointmentFindDto;
import com.unaux.dairo.api.domain.appointment.AppointmentResponseDto;
import com.unaux.dairo.api.domain.appointment.AppointmentUpdateDto;
import com.unaux.dairo.api.infra.errors.ResourceNotFoundException;
import com.unaux.dairo.api.service.AppointmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/appointment")
@Tag(name = "Appointment", description = "The Appointment API")
// @PreAuthorize("hasRole('ADMIN')")
// @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class AppointmentController {

  private final AppointmentService appointmentService;
  public static final String APPOINTMENT_BASE_URL = "/api/appointment/{id}";

  public AppointmentController(AppointmentService appointmentService) {
    this.appointmentService = appointmentService;
  }

  @PostMapping
  @Operation(summary = "Create a new appointment", description = "Creates a new appointment based on the given data")
  @ApiResponse(responseCode = "201", description = "Appointment created",
               content = @Content(schema = @Schema(implementation = AppointmentResponseDto.class),
               examples = @ExampleObject(
                 name = "example",
                 value = "{\"id\": 1, \"date\": \"2023-01-01\", \"time\": \"10:00:00\", \"condition\": \"pending\", \"notes\": \"First appointment\", \"product\": 1, \"employee\": 1, \"client\": 1, \"status\": true}"
               )))
  @ApiResponse(responseCode = "400", description = "Invalid input",
               content = @Content(
                 mediaType = "application/json",
                 examples = @ExampleObject(
                   name = "error",
                   value = "{\"message\": \"Invalid input\"}"
                 )))
  public ResponseEntity<?> createAppointment(UriComponentsBuilder uriComponentsBuilder,
      @RequestBody @Valid AppointmentCreateDto appointmentCreateDto) {
    // Extraemos los datos del DTO
    LocalDate date = appointmentCreateDto.date();
    LocalTime time = appointmentCreateDto.time();
    String notes = appointmentCreateDto.notes();
    int productId = appointmentCreateDto.productId();
    int employeeId = appointmentCreateDto.employeeId();
    int clientId = appointmentCreateDto.clientId();

    // *** validaciones menores (tipo, formato, etc)
    LocalDateTime dateTime = date.atTime(time);
    if (dateTime.isBefore(LocalDateTime.now())) {
      Map<String, Object> errorDetails = new HashMap<>();
      errorDetails.put("code", "BAD_REQUEST");
      errorDetails.put("message", "Error in the request");
      errorDetails.put("details", "The date and time is earlier than the current date.");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    try {
      Appointment appointment = appointmentService.save(date, time, notes, productId, employeeId, clientId);
      // creamos un DTO para retornar el objeto creado al frontend
      AppointmentResponseDto response = mapAppointmentToResponseDto(appointment);
      // Aquí crearemos una url que corresponde al objeto que se creó en la base de datos.
      String url = APPOINTMENT_BASE_URL.replace("{id}", String.valueOf(appointment.getId()));
      URI uri = uriComponentsBuilder.path(url).buildAndExpand(appointment.getId()).toUri();
      // URI url = uriComponentsBuilder
      //     .path(APPOINTMENT_BASE_URL)
      //     .buildAndExpand(appointment.getId())
      //     .toUri();

      return ResponseEntity.created(uri).body(response);

    } catch (ResourceNotFoundException e) {
      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .header(e.getClass().getSimpleName(), e.getMessage())
          .body(e.getMessage());
    }
  }

  @GetMapping
  @Operation(summary = "List all appointments", description = "Get a paginated list of all appointments")
  @ApiResponse(responseCode = "200", description = "Successful operation",
               content = @Content(schema = @Schema(implementation = Page.class),
               examples = @ExampleObject(
                 name = "example",
                 value = "{\"content\": [{\"id\": 1, \"date\": \"2023-01-01\", \"time\": \"10:00:00\", \"condition\": \"pending\", \"notes\": \"First appointment\", \"product\": 1, \"employee\": 1, \"client\": 1, \"status\": true}], \"pageable\": \"INSTANCE\", \"totalPages\": 1, \"totalElements\": 1}"
               )))
  public ResponseEntity<Page<AppointmentFindDto>> listAllAppointment(Pageable pagination) {
    Page<Appointment> ListAppointments = appointmentService.findAll(pagination);
    return ResponseEntity.ok(ListAppointments.map(AppointmentFindDto::new));
  }

  @GetMapping("/enabled")
  @Operation(summary = "List enabled appointments", description = "Get a paginated list of all enabled appointments")
  @ApiResponse(responseCode = "200", description = "Successful operation",
               content = @Content(schema = @Schema(implementation = Page.class),
               examples = @ExampleObject(
                 name = "example",
                 value = "{\"content\": [{\"id\": 1, \"date\": \"2023-01-01\", \"time\": \"10:00:00\", \"condition\": \"pending\", \"notes\": \"First appointment\", \"product\": 1, \"employee\": 1, \"client\": 1, \"status\": true}], \"pageable\": \"INSTANCE\", \"totalPages\": 1, \"totalElements\": 1}"
               )))
  public ResponseEntity<Page<AppointmentFindDto>> listEnabledStatusAppointment(Pageable pagination) {
    Page<Appointment> ListAppointments = appointmentService.findEnabled(pagination);
    return ResponseEntity.ok(ListAppointments.map(AppointmentFindDto::new));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Find appointment by ID", description = "Returns a single appointment")
  @ApiResponse(responseCode = "200", description = "Successful operation",
               content = @Content(schema = @Schema(implementation = AppointmentResponseDto.class),
               examples = @ExampleObject(
                 name = "example",
                 value = "{\"id\": 1, \"date\": \"2023-01-01\", \"time\": \"10:00:00\", \"condition\": \"pending\", \"notes\": \"First appointment\", \"product\": 1, \"employee\": 1, \"client\": 1, \"status\": true}"
               )))
  @ApiResponse(responseCode = "404", description = "Appointment not found",
               content = @Content(
                 mediaType = "application/json",
                 examples = @ExampleObject(
                   name = "error",
                   value = "{\"code\": \"RESOURCE_NOT_FOUND\", \"message\": \"The requested resource was not found\", \"details\": \"No record with the ID 1 was found in the database.\"}"
                 )))
  public ResponseEntity<?> findAppointment(@PathVariable int id) {
    // *** No hay validaciones menores para realizar
    Optional<Appointment> appointmentOptional = appointmentService.findById(id);

    if (appointmentOptional.isEmpty()) {
      Map<String, Object> errorDetails = new HashMap<>();
      errorDetails.put("code", "RESOURCE_NOT_FOUND");
      errorDetails.put("message", "The requested resource was not found");
      errorDetails.put("details", "No record with the ID " + id + " was found in the database.");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);

      // Enviar un código de estado HTTP 404 Not Found con un mensaje en el cuerpo
      //! return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The requested client is not found");
    }
    // Creamos un DTO para retornar el objeto al frontend
    Appointment appointment = appointmentOptional.get();
    AppointmentResponseDto response = mapAppointmentToResponseDto(appointment);

    return ResponseEntity.ok(response);
  }

  @PutMapping
  @Transactional
  @Operation(summary = "Update an existing appointment", description = "Updates an appointment based on the given data")
  @ApiResponse(responseCode = "200", description = "Successful operation",
               content = @Content(schema = @Schema(implementation = AppointmentResponseDto.class),
               examples = @ExampleObject(
                 name = "example",
                 value = "{\"id\": 1, \"date\": \"2023-01-01\", \"time\": \"10:00:00\", \"condition\": \"pending\", \"notes\": \"First appointment\", \"product\": 1, \"employee\": 1, \"client\": 1, \"status\": true}"
               )))
  @ApiResponse(responseCode = "400", description = "Invalid input",
               content = @Content(
                 mediaType = "application/json",
                 examples = @ExampleObject(
                   name = "error",
                   value = "{\"message\": \"Invalid input\"}"
                 )))
  @ApiResponse(responseCode = "404", description = "Appointment not found",
               content = @Content(
                 mediaType = "application/json",
                 examples = @ExampleObject(
                   name = "error",
                   value = "{\"message\": \"Appointment not found\"}"
                 )))
  public ResponseEntity<?> updateAppointment(
      @RequestBody @Valid AppointmentUpdateDto appointmentUpdateDto) {
    // Extraemos los datos
    int id = appointmentUpdateDto.id();
    LocalDate date = appointmentUpdateDto.date();
    LocalTime time = appointmentUpdateDto.time();
    int productId = appointmentUpdateDto.productId();
    int employeeId = appointmentUpdateDto.employeeId();
    String notes = appointmentUpdateDto.notes();

    // *** validaciones menores (tipo, formato, etc)
    LocalDateTime dateTime = date.atTime(time);

    if (dateTime.isBefore(LocalDateTime.now())) {
      Map<String, Object> errorDetails = new HashMap<>();
      errorDetails.put("code", "BAD_REQUEST");
      errorDetails.put("message", "Error in the request");
      errorDetails.put("details", "The date and time is earlier than the current date.");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    try {
      Appointment appointment = appointmentService.update(
        id, date, time, productId, employeeId, notes);
      // Creamos un DTO para retornar el objeto al frontend
      AppointmentResponseDto response = mapAppointmentToResponseDto(appointment);

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
  @Operation(summary = "Delete an appointment", description = "Deletes an appointment")
  @ApiResponse(responseCode = "204", description = "Successful operation")
  @ApiResponse(responseCode = "404", description = "Resource not found",
               content = @Content(
                 mediaType = "application/json",
                 examples = @ExampleObject(
                   name = "error",
                   value = "{\"message\": \"Resource not found\"}"
                 )))
  public ResponseEntity<?> deleteAppointment(@PathVariable int id) {
    try {
      appointmentService.delete(id);
      // Retornamos una respuesta vacía
      return ResponseEntity.noContent().build();
    } catch (EntityNotFoundException e) {
      String errorMessage = "Resource not found with ID: %d".formatted(id);
      return ResponseEntity.badRequest().body(errorMessage);
    }
  }

  private AppointmentResponseDto mapAppointmentToResponseDto(Appointment appointment) {
    return new AppointmentResponseDto(
        appointment.getId(),
        appointment.getDate(),
        appointment.getTime(),
        appointment.getCondition(),
        appointment.getNotes(),
        appointment.getProduct().getId(),
        appointment.getEmployee().getId(),
        appointment.getClient().getId(),
        appointment.isStatus());
  }
}
