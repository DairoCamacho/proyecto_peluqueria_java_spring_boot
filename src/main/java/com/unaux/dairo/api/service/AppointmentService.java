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
  // Inyección de dependencias por constructor (mejor práctica)
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

  /**
     * Guarda una nueva cita en el repositorio.
     *
     * @param date La fecha de la cita.
     * @param time La hora de la cita.
     * @param notes Notas adicionales sobre la cita.
     * @param productId El identificador del producto asociado a la cita.
     * @param employeeId El identificador del empleado asociado a la cita.
     * @param clientId El identificador del cliente asociado a la cita.
     * @return La cita guardada.
     * @throws ResourceNotFoundException Si no se encuentra el producto, el empleado o el cliente con los ID proporcionados.
     */
  public Appointment save(LocalDate date, LocalTime time, String notes, int productId, int employeeId, int clientId) {
    
    Product product = productService.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("Product not found with the ID: " + productId));
    Employee employee = employeeService.findById(employeeId)
        .orElseThrow(() -> new ResourceNotFoundException("Employee not found with the ID: " + employeeId));
    Client client = clientService.findById(clientId)
        .orElseThrow(() -> new ResourceNotFoundException("Client not found with the ID: " + clientId));

    return appointmentRepository.save(new Appointment(date, time, product, employee, client, notes));
  }

  /**
   * Obtiene una página de todas las citas.
   *
   * @param pagination Objeto de paginación para definir el tamaño de página y la página actual.
   * @return Una página de citas.
   */
  public Page<Appointment> findAll(Pageable pagination) {
    return appointmentRepository.findAll(pagination);
  }

  /**
     * Obtiene una página de todas las citas activas (no canceladas).
     *
     * @param pagination Objeto de paginación para definir el tamaño de página y la página actual.
     * @return Una página de citas activas.
     */
  public Page<Appointment> findEnabled(Pageable pagination) {
    return appointmentRepository.findByStatusTrue(pagination);
  }

  /**
     * Obtiene una cita por su ID.
     *
     * @param id El identificador de la cita.
     * @return Un Optional que contiene la cita si se encuentra, o está vacío si no se encuentra.
     */
  public Optional<Appointment> findById(int id) {
    return appointmentRepository.findById(id);
  }

  /**
     * Actualiza una cita existente.
     *
     * @param id El identificador de la cita a actualizar.
     * @param date La nueva fecha de la cita.
     * @param time La nueva hora de la cita.
     * @param productId El nuevo identificador del producto asociado.
     * @param employeeId El nuevo identificador del empleado asociado.
     * @param notes Las nuevas notas de la cita.
     * @return La cita actualizada.
     * @throws ResourceNotFoundException Si la cita, el producto, el empleado o el cliente no se encuentran.
     */
public Appointment update(int id, LocalDate date, LocalTime time, int productId, int employeeId, String notes) {
    
    Appointment appointment = appointmentRepository.getReferenceById(id);
    Product product = productService.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("Product not found with the ID: " + productId));
    Employee employee = employeeService.findById(employeeId)
        .orElseThrow(() -> new ResourceNotFoundException("Employee not found with the ID: " + employeeId));
    
    appointment.update(date, time, product, employee, notes);
    
    return appointmentRepository.save(appointment);
  }


  /**
     * Eliminación lógica de una cita.
     *
     * @param id El identificador de la cita a cancelar.
     * @throws EntityNotFoundException Si la cita no se encuentra.
     */
  public void delete(int id) {
    
    Appointment appointment = appointmentRepository.getReferenceById(id);
    
    appointment.setCondition("canceled");
    
    appointment.inactivate();
  }
}
