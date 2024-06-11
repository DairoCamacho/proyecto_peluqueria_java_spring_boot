package com.unaux.dairo.api.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.unaux.dairo.api.domain.client.Client;
import com.unaux.dairo.api.domain.employee.Employee;
import com.unaux.dairo.api.domain.hairsalon.HairSalon;
import com.unaux.dairo.api.infra.errors.ResourceNotFoundException;
import com.unaux.dairo.api.repository.EmployeeRepository;

@Service
public class EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final ClientService clientService;
  private final HairSalonService hairSalonService;

  public EmployeeService(EmployeeRepository employeeRepository, ClientService clientService,
      HairSalonService hairSalonService) {
    this.employeeRepository = employeeRepository;
    this.clientService = clientService;
    this.hairSalonService = hairSalonService;
  }

  public Employee save(int clientId, String position, LocalDate hireDate, int hairSalonId) {
    // *** validamos que no se pueda modificar el ID 1, ya que es el admin del sistema
    if (clientId == 1) {
      throw new ResourceNotFoundException("ID not found");
    }

    Client client = clientService.findById(clientId)
        .orElseThrow(() -> new ResourceNotFoundException("Client not found with the ID: " + clientId));

    HairSalon hairSalon = hairSalonService.findById(hairSalonId)
        .orElseThrow(
            () -> new ResourceNotFoundException("HairSalon not found with the ID: " + hairSalonId));

    return employeeRepository.save(new Employee(client, position, hireDate, hairSalon));
  }

  public Page<Employee> findAll(Pageable pagination) {
    return employeeRepository.findAll(pagination);
  }

  public Optional<Employee> findById(int id) {
    return employeeRepository.findById(id);
  }

  public Employee update(int id, LocalDate hireDate, String position, Boolean status, LocalDate terminationDate,
      int hairSalonId) {
    // con el ID Buscamos la Entidad a actualizar
    Employee employee = employeeRepository.getReferenceById(id);
    /* validamos que HairSalon se quiere cambiar y que existe el nuevo HairSalon
    por defecto HairSalon guardará el mismo valor que ya tiene, pero si viene un valor en DTO
    (número mayor que cero) quiere decir que se quiere actualizar el HairSalon
    */
    HairSalon hairSalon = employee.getHairSalon();
    if (hairSalonId > 0) {
      hairSalon = hairSalonService.findById(hairSalonId)
          .orElseThrow(() -> new ResourceNotFoundException("Employee not found with the ID: " + hairSalonId));
    }
    // Actualizamos la Entidad con los datos del DTO
    employee.update(position, hireDate, terminationDate, status, hairSalon);
    // retornamos la Entidad ya actualizada
    return employee;
  }

  public void delete(int id) {
    // con el ID Buscamos la Entidad para desactivar
    Employee employee = employeeRepository.getReferenceById(id);
    // Desactivamos - borrado lógico
    employee.inactivate();
    // si la fecha de despido no está la agregamos
    if (employee.getTerminationDate() == null) {
      employee.setTerminationDate(LocalDate.now());
    }
  }
}
