package com.unaux.dairo.api.service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.unaux.dairo.api.domain.client.Client;
import com.unaux.dairo.api.domain.employee.Employee;
import com.unaux.dairo.api.domain.hairsalon.HairSalon;
import com.unaux.dairo.api.domain.product.Product;
import com.unaux.dairo.api.infra.errors.ResourceNotFoundException;
import com.unaux.dairo.api.repository.EmployeeRepository;

import jakarta.annotation.Nullable;

@Service
public class EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final ClientService clientService;
  private final HairSalonService hairSalonService;
  private final ProductService productService;

  public EmployeeService(EmployeeRepository employeeRepository, ClientService clientService,
      HairSalonService hairSalonService, ProductService productService) {
    this.employeeRepository = employeeRepository;
    this.clientService = clientService;
    this.hairSalonService = hairSalonService;
    this.productService = productService;
  }

  public Employee save(int clientId, String position, LocalDate hireDate, int hairSalonId, Set<Integer> products) {
    // *** validamos que no se pueda modificar el ID 1, ya que es el admin del sistema
    if (clientId == 1) {
      throw new ResourceNotFoundException("ID not found");
    }

    Client client = clientService.findById(clientId)
        .orElseThrow(() -> new ResourceNotFoundException("Client not found with the ID: " + clientId));

    HairSalon hairSalon = hairSalonService.findById(hairSalonId)
        .orElseThrow(() -> new ResourceNotFoundException("HairSalon not found with the ID: " + hairSalonId));

    Set<Product> listProducts = products.stream().map(productId -> productService.findById(productId).orElse(null))
        .filter(product -> product != null) // Filtra los productos no nulos (existentes)
        .collect(Collectors.toSet());

    if (listProducts.isEmpty()) {
      throw new ResourceNotFoundException("No products found with the provided IDs");
    }

    return employeeRepository.save(new Employee(client, position, hireDate, hairSalon, listProducts));
  }

  public Page<Employee> findAll(Pageable pagination) {
    return employeeRepository.findAll(pagination);
  }

  public Optional<Employee> findById(int id) {
    return employeeRepository.findById(id);
  }

  public Employee update(int id, @Nullable LocalDate hireDate, @Nullable String position, @Nullable Boolean status, @Nullable LocalDate terminationDate,
  @Nullable Integer hairSalonId, @Nullable Set<Integer> productsId) {
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
    // Filtrar productos existentes y convertirlos a un Set<Product>
    Set<Product> listProducts = null;
    if (productsId != null) {
      listProducts = productsId.stream().map(productId -> productService.findById(productId).orElse(null))
          .filter(product -> product != null) // Filtra los productos no nulos (existentes)
          .collect(Collectors.toSet());
      if (listProducts.isEmpty()) {
        throw new ResourceNotFoundException("No products found with the provided IDs");
      }
    }
    // Actualizamos la Entidad con los datos del DTO
    employee.update(position, hireDate, terminationDate, status, hairSalon, listProducts);
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
