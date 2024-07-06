package com.unaux.dairo.api.infra.security;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.unaux.dairo.api.domain.appointment.Appointment;
import com.unaux.dairo.api.domain.client.Client;
import com.unaux.dairo.api.domain.employee.Employee;
import com.unaux.dairo.api.domain.hairsalon.HairSalon;
import com.unaux.dairo.api.domain.product.Product;
import com.unaux.dairo.api.domain.user.Role;
import com.unaux.dairo.api.domain.user.User;
import com.unaux.dairo.api.domain.workinghours.WorkingHours;
import com.unaux.dairo.api.repository.AppointmentRepository;
import com.unaux.dairo.api.repository.ClientRepository;
import com.unaux.dairo.api.repository.EmployeeRepository;
import com.unaux.dairo.api.repository.HairSalonRepository;
import com.unaux.dairo.api.repository.ProductRepository;
import com.unaux.dairo.api.repository.UserRepository;
import com.unaux.dairo.api.repository.WorkingHoursRepository;

@Component
public class DataLoader implements CommandLineRunner {

  private final ClientRepository clientRepository;
  private final UserRepository userRepository;
  private final HairSalonRepository hairSalonRepository;
  private final EmployeeRepository employeeRepository;
  private final ProductRepository productRepository;
  private final AppointmentRepository appointmentRepository;
  private final WorkingHoursRepository workingHoursRepository;

  // inyectamos los repositorios
  public DataLoader(ClientRepository clientRepository, UserRepository userRepository,
      HairSalonRepository hairSalonRepository, EmployeeRepository employeeRepository,
      ProductRepository productRepository, AppointmentRepository appointmentRepository,
      WorkingHoursRepository workingHoursRepository) {
    this.clientRepository = clientRepository;
    this.userRepository = userRepository;
    this.hairSalonRepository = hairSalonRepository;
    this.employeeRepository = employeeRepository;
    this.productRepository = productRepository;
    this.appointmentRepository = appointmentRepository;
    this.workingHoursRepository = workingHoursRepository;
  }

  @Override
  // @Transactional
  public void run(String... args) throws Exception {
    // Crear usuario
    String email1 = "user@admin.com";
    if (!userRepository.existsByEmail(email1)) {
      // Crear y guardar usuarios
      User user1 = User.builder().email(email1).password(encryptPassword("admin")).role(Role.ADMIN).status(true)
          .build();
      User userCreated1 = userRepository.save(user1);

      Client client1 = Client.builder().birthday(LocalDate.of(1990, 1, 1)).lastName("Admin").name("User")
          .phone("3201111111").type("ADMIN").status(true).user(userCreated1).build();

      clientRepository.save(client1);
    }

    // Crear usuario
    String email2 = "aleja@empleada.com";
    if (!userRepository.existsByEmail(email2)) {
      // Crear y guardar usuarios
      User user2 = User.builder().email(email2).password(encryptPassword("admin")).role(Role.EMPLOYEE).status(true)
          .build();
      User userCreated2 = userRepository.save(user2);

      Client client2 = Client.builder().birthday(LocalDate.of(1990, 1, 2)).lastName("Alejandra").name("Alejandra")
          .phone("3201111112").type("EMPLOYEE").status(true).user(userCreated2).build();

      clientRepository.save(client2);
    }

    // Crear usuario
    String email3 = "maria@empleada.com";
    if (!userRepository.existsByEmail(email3)) {
      // Crear y guardar usuarios
      User user3 = User.builder().email(email3).password(encryptPassword("admin")).role(Role.EMPLOYEE).status(true)
          .build();
      User userCreated3 = userRepository.save(user3);

      Client client3 = Client.builder().birthday(LocalDate.of(1990, 1, 3)).lastName("Maria").name("Maria")
          .phone("3201111113").type("EMPLOYEE").status(true).user(userCreated3).build();

      clientRepository.save(client3);
    }

    // Crear usuario
    String email4 = "pedro@cliente.com";
    if (!userRepository.existsByEmail(email4)) {
      // Crear y guardar usuarios
      User user4 = User.builder().email(email4).password(encryptPassword("admin")).role(Role.CLIENT).status(true)
          .build();
      User userCreated4 = userRepository.save(user4);

      Client client4 = Client.builder().birthday(LocalDate.of(1990, 1, 4)).lastName("Perez").name("Pedro")
          .phone("3201111114").type("new client").status(true).user(userCreated4).build();

      clientRepository.save(client4);
    }

    // Crear peluquería
    String hairSalonName = "Peluquería de Prueba";
    if (!hairSalonRepository.existsByName(hairSalonName)) {
      HairSalon hairSalon = HairSalon.builder().name(hairSalonName).phone("tel9997890").address("Calle 123, 12345")
          .neighborhood("Centro").city("Manizales").country("Colombia").status(true).build();

      hairSalonRepository.save(hairSalon);
    }

    // Crear Producto
    int product1 = 1;
    if (!productRepository.existsById(product1)) {
      Product product = Product.builder().name("Corte de pelo").price(11000).duration(LocalTime.of(0, 20)) // 20 minutos
          .hairSalon(hairSalonRepository.getReferenceById(1)) // salon de prueba
          .status(true).build();

      productRepository.save(product);
    }

    // Crear Producto
    int product2 = 2;
    if (!productRepository.existsById(product2)) {
      Product product = Product.builder().name("Cepillado de pelo").price(12000).duration(LocalTime.of(0, 30)) // 30 minutos
          .hairSalon(hairSalonRepository.getReferenceById(1)) // salon de prueba
          .status(true).build();

      productRepository.save(product);
    }

    // Crear Producto
    int product3 = 3;
    if (!productRepository.existsById(product3)) {
      Product product = Product.builder().name("Pedicure").price(13000).duration(LocalTime.of(0, 45)) // 45 minutos
          .hairSalon(hairSalonRepository.getReferenceById(1)) // salon de prueba
          .status(true).build();

      productRepository.save(product);
    }

    // Crear Producto
    int product4 = 4;
    if (!productRepository.existsById(product4)) {
      Product product = Product.builder().name("Manicure").price(14000).duration(LocalTime.of(1, 0)) // 1 hora
          .hairSalon(hairSalonRepository.getReferenceById(1)) // salon de prueba
          .status(true).build();

      productRepository.save(product);
    }

    // Crear Producto
    int product5 = 5;
    if (!productRepository.existsById(product5)) {
      Product product = Product.builder().name("Tinturado de pelo").price(15000).duration(LocalTime.of(1, 30)) // 1 hora y 30 minutos
          .hairSalon(hairSalonRepository.getReferenceById(1)) // salon de prueba
          .status(true).build();

      productRepository.save(product);
    }

    // Crear empleado
    int employee1 = 1;
    Set<Product> listProducts1 = Stream.of(productRepository.findById(1).orElse(null), productRepository.findById(2).orElse(null)).collect(Collectors.toSet());
    // Set<Product> listProducts1 = Set.of(productRepository.getReferenceById(1), productRepository.getReferenceById(2));// productos
    if (!employeeRepository.existsById(employee1)) {
      Employee employee = Employee.builder().client(clientRepository.getReferenceById(2)) // Alejandra
          .hairSalon(hairSalonRepository.getReferenceById(1)).hireDate(LocalDate.of(2024, 04, 01)).position("Peluquero")
          .products(listProducts1).status(true).build();
      employeeRepository.save(employee);
    }
    // Crear empleado
    int employee2 = 2;
    Set<Product> listProducts2 = Stream.of(productRepository.findById(3).orElse(null), productRepository.findById(4).orElse(null)).collect(Collectors.toSet());
    // Set<Product> listProducts2 = Set.of(productRepository.getReferenceById(3), productRepository.getReferenceById(4)); // producto: Corte de Pelo
    if (!employeeRepository.existsById(employee2)) {
      Employee employee = Employee.builder().client(clientRepository.getReferenceById(3)) // Maria
          .hairSalon(hairSalonRepository.getReferenceById(1)).hireDate(LocalDate.of(2024, 04, 02))
          .position("Manicurista").products(listProducts2).status(true).build();
      employeeRepository.save(employee);
    }

    // Crear workingHour
    int workingHour1 = 1;
    if (!workingHoursRepository.existsById(workingHour1)) {
      WorkingHours workingHours = WorkingHours.builder().startDate(LocalDateTime.of(2024, 8, 01, 8, 00))
          .endDate(LocalDateTime.of(2024, 8, 01, 14, 00)).employee(employeeRepository.getReferenceById(1)) // Alejandra
          .status(true).build();

      workingHoursRepository.save(workingHours);
    }

    // Crear workingHour
    int workingHour2 = 2;
    if (!workingHoursRepository.existsById(workingHour2)) {
      WorkingHours workingHours = WorkingHours.builder().startDate(LocalDateTime.parse("2024-08-01T14:00"))
          .endDate(LocalDateTime.parse("2024-08-01T20:00")).employee(employeeRepository.getReferenceById(2)) // Maria
          .status(true).build();

      workingHoursRepository.save(workingHours);
    }

    // Crear workingHour
    int workingHour3 = 3;
    if (!workingHoursRepository.existsById(workingHour3)) {
      WorkingHours workingHours = WorkingHours.builder().startDate(LocalDateTime.of(2024, 8, 01, 14, 00))
          .endDate(LocalDateTime.of(2024, 8, 01, 15, 00)).employee(employeeRepository.getReferenceById(1)) // Alejandra
          .status(false).build();

      workingHoursRepository.save(workingHours);
    }

    // Crear workingHour
    int workingHour4 = 4;
    if (!workingHoursRepository.existsById(workingHour4)) {
      WorkingHours workingHours = WorkingHours.builder().startDate(LocalDateTime.parse("2024-08-01T20:00"))
          .endDate(LocalDateTime.parse("2024-08-01T21:00")).employee(employeeRepository.getReferenceById(2)) // Maria
          .status(false).build();

      workingHoursRepository.save(workingHours);
    }

    // Crear Cita
    int appointment1 = 1;
    if (!appointmentRepository.existsById(appointment1)) {
      Appointment appointment = Appointment.builder().date(LocalDate.of(2024, 8, 1)).time(LocalTime.of(8, 0)) // 8 am
          .notes("quiero un corte de pelo en capas").condition("pending").product(productRepository.getReferenceById(1)) // Corte de pelo
          .employee(employeeRepository.getReferenceById(1)) // Alejandra
          .client(clientRepository.getReferenceById(4)) // Pedro
          .status(true).build();

      appointmentRepository.save(appointment);
    }

    // Crear Cita
    int appointment2 = 2;
    if (!appointmentRepository.existsById(appointment2)) {
      Appointment appointment = Appointment.builder().date(LocalDate.of(2024, 8, 1)).time(LocalTime.of(14, 0)) // 2 pm
          .notes(null).condition("pending").product(productRepository.getReferenceById(2)) // Manicure
          .employee(employeeRepository.getReferenceById(2)) // Maria
          .client(clientRepository.getReferenceById(4)) // Pedro
          .status(true).build();

      appointmentRepository.save(appointment);
    }
  }

  public String encryptPassword(String password) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    return encoder.encode(password);
  }
}
