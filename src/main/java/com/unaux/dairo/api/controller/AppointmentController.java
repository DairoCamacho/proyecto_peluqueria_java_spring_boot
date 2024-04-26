package com.unaux.dairo.api.controller;

import com.unaux.dairo.api.domain.appointment.Appointment;
import com.unaux.dairo.api.domain.appointment.AppointmentCreateDto;
import com.unaux.dairo.api.domain.appointment.AppointmentFindDto;
import com.unaux.dairo.api.domain.appointment.AppointmentResponseDto;
import com.unaux.dairo.api.domain.appointment.AppointmentUpdateDto;
import com.unaux.dairo.api.domain.client.Client;
import com.unaux.dairo.api.domain.employee.Employee;
import com.unaux.dairo.api.domain.service.Service;
import com.unaux.dairo.api.repository.AppointmentRepository;
import com.unaux.dairo.api.repository.ClientRepository;
import com.unaux.dairo.api.repository.EmployeeRepository;
import com.unaux.dairo.api.repository.ServiceRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.net.URI;
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
@RequestMapping("api/appointment")
public class AppointmentController {

  private final AppointmentRepository appointmentRepository;
  private final ServiceRepository serviceRepository;
  private final EmployeeRepository employeeRepository;
  private final ClientRepository clientRepository;

  public AppointmentController(
    AppointmentRepository appointmentRepository,
    ServiceRepository serviceRepository,
    EmployeeRepository employeeRepository,
    ClientRepository clientRepository
  ) {
    this.appointmentRepository = appointmentRepository;
    this.serviceRepository = serviceRepository;
    this.employeeRepository = employeeRepository;
    this.clientRepository = clientRepository;
  }

  @PostMapping
  public ResponseEntity<AppointmentResponseDto> createAppointment(
    @RequestBody @Valid AppointmentCreateDto appointmentCreateDto,
    UriComponentsBuilder uriComponentsBuilder
  ) {
    Service service = serviceRepository
      .findById(appointmentCreateDto.service())
      .orElseThrow(() -> new RuntimeException("Service not found"));

    Employee employee = employeeRepository
      .findById(appointmentCreateDto.employee())
      .orElseThrow(() -> new RuntimeException("Employee not found"));

    Client client = clientRepository
      .findById(appointmentCreateDto.client())
      .orElseThrow(() -> new RuntimeException("Client not found"));

    Appointment appointment = appointmentRepository.save(
      new Appointment(appointmentCreateDto, service, employee, client)
    );

    AppointmentResponseDto response = new AppointmentResponseDto(
      appointment.getId(),
      appointment.getDate(),
      appointment.getTime(),
      appointment.getStatus(),
      appointment.getNotes(),
      appointment.getService().getId(),
      appointment.getEmployee().getId(),
      appointment.getClient().getId()
    );
    URI url = uriComponentsBuilder
      .path("api/appointment/{id}")
      .buildAndExpand(appointment.getId())
      .toUri();

    return ResponseEntity.created(url).body(response);
  }

  @GetMapping
  public ResponseEntity<Page<AppointmentFindDto>> listAllAppointment(
    Pageable pagination
  ) {
    return ResponseEntity.ok(
      appointmentRepository.findAll(pagination).map(AppointmentFindDto::new)
    );
  }

  @GetMapping("/{id}")
  public ResponseEntity<AppointmentResponseDto> findAppointment(
    @PathVariable String id
  ) {
    Optional<Appointment> appointmentOptional = appointmentRepository.findById(
      id
    );
    if (!appointmentOptional.isPresent()) {
      return ResponseEntity.notFound().build();
    }
    Appointment appointment = appointmentOptional.get();

    AppointmentResponseDto response = new AppointmentResponseDto(
      appointment.getId(),
      appointment.getDate(),
      appointment.getTime(),
      appointment.getStatus(),
      appointment.getNotes(),
      appointment.getService().getId(),
      appointment.getEmployee().getId(),
      appointment.getClient().getId()
    );

    return ResponseEntity.ok(response);
  }

  @PutMapping
  @Transactional
  public ResponseEntity<AppointmentResponseDto> updateAppointment(
    @RequestBody @Valid AppointmentUpdateDto appointmentUpdateDto
  ) {
    Appointment appointment = appointmentRepository.getReferenceById(
      appointmentUpdateDto.id()
    );
    Service service = serviceRepository.getReferenceById(
      appointmentUpdateDto.service()
    );
    Employee employee = employeeRepository.getReferenceById(
      appointmentUpdateDto.employee()
    );

    /* 
    // ! el cliente no se permitirá modificar
    Client client = clientRepository.getReferenceById(
      appointmentUpdateDto.client()
    );
     */

    appointment.update(appointmentUpdateDto, service, employee);

    AppointmentResponseDto response = new AppointmentResponseDto(
      appointment.getId(),
      appointment.getDate(),
      appointment.getTime(),
      appointment.getStatus(),
      appointment.getNotes(),
      appointment.getService().getId(),
      appointment.getEmployee().getId(),
      appointment.getClient().getId()
    );

    return ResponseEntity.ok(response);
  }
  
  // OJO: delete físico
  @DeleteMapping("/{id}")
  @Transactional
  public void deleteAppointment(@PathVariable String id) {
    Appointment appointment = appointmentRepository
      .findById(id)
      .orElseThrow(() -> new RuntimeException("Appointment not found"));

    appointmentRepository.delete(appointment);
  }
}
