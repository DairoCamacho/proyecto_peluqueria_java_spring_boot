package com.unaux.dairo.api.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.unaux.dairo.api.domain.appointment.Appointment;
import com.unaux.dairo.api.domain.client.Client;
import com.unaux.dairo.api.domain.employee.Employee;
import com.unaux.dairo.api.domain.product.Product;
import com.unaux.dairo.api.infra.errors.ResourceNotFoundException;
import com.unaux.dairo.api.repository.AppointmentRepository;

@Service
public class AppointmentService {

  private final AppointmentRepository appointmentRepository;
  private final ProductService productService;
  private final EmployeeService employeeService;
  private final ClientService clientService;

  public AppointmentService(AppointmentRepository appointmentRepository,
      ProductService productService,
      EmployeeService employeeService,
      ClientService clientService) {
    this.appointmentRepository = appointmentRepository;
    this.productService = productService;
    this.employeeService = employeeService;
    this.clientService = clientService;
  }

  public Appointment save(LocalDate date, LocalTime time, String notes, int productId, int employeeId, int clientId) {
    
    Product product = productService.findById(productId)
        .orElseThrow(
            () -> new ResourceNotFoundException("Service not found with the ID: " + productId));

    Employee employee = employeeService
        .findById(employeeId)
        .orElseThrow(
            () -> new ResourceNotFoundException(
                "Employee not found with the ID: " + employeeId));

    Client client = clientService
        .findById(clientId)
        .orElseThrow(
            () -> new ResourceNotFoundException("Client not found with the ID: " + clientId));

    return appointmentRepository.save(new Appointment(date, time, product, employee, client, notes));
  }

  public Page<Appointment> findAll(Pageable pagination) {
    return appointmentRepository.findAll(pagination);
  }

  public Optional<Appointment> findById(int id) {
    return appointmentRepository.findById(id);
  }

  public Appointment update(int id, LocalDate date, LocalTime time, int productId, int employeeId, String notes) {
    // con el ID Buscamos la Entidad a actualizar
    Appointment appointment = appointmentRepository.getReferenceById(id);
    // validamos que el producto existe
    Product product = productService
        .findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("Product not found with the ID: " + productId));
    // validamos que el Empleado existe
    Employee employee = employeeService
        .findById(employeeId)
        .orElseThrow(() -> new ResourceNotFoundException("Employee not found with the ID: " + employeeId));
    // Actualizamos la Entidad con los datos del DTO
    appointment.update(date, time, product, employee, notes);
    // retornamos la Entidad ya actualizada
    return appointmentRepository.save(appointment);
  }

  public void delete(int id) {
    // con el ID Buscamos la Entidad para eliminar
    Appointment appointment = appointmentRepository.getReferenceById(id);
    // cambiamos el campo condition, poniendo canceled
    appointment.setCondition("canceled");
    // Desactivamos - borrado lógico
    appointment.inactivate();
  }
}
