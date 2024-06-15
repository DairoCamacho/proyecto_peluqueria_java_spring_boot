package com.unaux.dairo.api.Service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.unaux.dairo.api.domain.appointment.Appointment;
import com.unaux.dairo.api.domain.client.Client;
import com.unaux.dairo.api.domain.employee.Employee;
import com.unaux.dairo.api.domain.product.Product;
import com.unaux.dairo.api.infra.errors.ResourceNotFoundException;
import com.unaux.dairo.api.repository.AppointmentRepository;
import com.unaux.dairo.api.service.AppointmentService;
import com.unaux.dairo.api.service.ClientService;
import com.unaux.dairo.api.service.EmployeeService;
import com.unaux.dairo.api.service.ProductService;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

  /*
  * GIVEN - dado = condición previa o configuración. Se especifica el escenario,
  * las predicciones. También puede aparecer como ARRANGE - DISPONER
  * WHEN - cuando = acción o comportamiento que vamos a probar. Las condiciones de
  * las acciones que se van a ejecutar. También puede aparecer como ACT - ACCIÓN
  * THEN - entonces = verificar la salida. El resultado esperado, las validaciones a
  * realizar. También puede aparecer como ASSERT - CONFIRMAR
  * 
  * EJEMPLO:
  * GIVEN: Dado que el usuario no ha introducido ningún dato en el formulario.
  * WHEN: Cuando hace clic en el botón Enviar.
  * THEN: Entonces se deben mostrar los mensajes de validación apropiados.
  */

  // @MockBean: agrega objetos simulados al contexto de la app, remplazará
  // cualquier Bean del mismo tipo en el contexto de la app
  @Mock
  private AppointmentRepository appointmentRepository;
  @Mock
  private ProductService productService;
  @Mock
  private EmployeeService employeeService;
  @Mock
  private ClientService clientService;
  @InjectMocks
  private AppointmentService appointmentService;
  // @Captor para capturar el argumento del método save del servicio.
  @Captor
  private ArgumentCaptor<Appointment> captor = ArgumentCaptor.forClass(Appointment.class);

  // private ArgumentCaptor<Appointment> captor; // también funciona así
  @Test
  void testSave_ShouldReturnCreatedAppointment() {
    // GIVEN
    // Configurar los mocks
    LocalDate date = LocalDate.now();
    LocalTime time = LocalTime.now();
    int productId = 1;
    int employeeId = 1;
    int clientId = 1;
    String notes = "Notas de prueba";

    Product product = new Product();
    Employee employee = new Employee();
    Client client = new Client();

    when(productService.findById(productId)).thenReturn(Optional.of(product));
    when(employeeService.findById(employeeId)).thenReturn(Optional.of(employee));
    when(clientService.findById(clientId)).thenReturn(Optional.of(client));

    // WHEN
    // Llamar al método que se está probando
    appointmentService.save(date, time, notes, productId, employeeId, clientId);

    // THEN
    // aquí no hay validaciones sobre result porque no está simulada (mock) la respuesta del servicio.
    /*
    * ENFOQUE VERIFICANDO EL LLAMADO AL REPOSITORIO Y VERIFICANDO LA RESPUESTA (appointmentCaptor)
    * verify(appointmentRepository).save(...): Esta parte utiliza Mockito para
    * verificar si se llamó al método save del repositorio appointmentRepository.
    * appointmentCaptor.capture() captura el objeto Appointment después de ser
    * guardado.
    */

    verify(appointmentRepository, times(1)).save(captor.capture());
    // verify(appointmentRepository, times(1)).save(any(Appointment.class));
    // verify(appointmentRepository).save(captor.capture());
    Appointment savedAppointment = captor.getValue();
    assertThat(savedAppointment).isNotNull();
    assertThat(savedAppointment.getDate()).isEqualTo(date);
    assertThat(savedAppointment.getTime()).isEqualTo(time);
    assertThat(savedAppointment.getNotes()).isEqualTo(notes);
    assertThat(savedAppointment.getProduct()).isEqualTo(product);
    assertThat(savedAppointment.getEmployee()).isEqualTo(employee);
    assertThat(savedAppointment.getClient()).isEqualTo(client);
  }

  @Test
  void testSave_ShouldThrowResourceNotFoundException_WhenProductNotFound() {
    // GIVEN
    // Configurar los mocks
    LocalDate date = LocalDate.now();
    LocalTime time = LocalTime.now();
    int productId = 1;
    int employeeId = 1;
    int clientId = 1;
    String notes = "Notas de prueba";

    when(productService.findById(productId)).thenReturn(Optional.empty());

    // WHEN THEN
    assertThrows(ResourceNotFoundException.class, () -> {
      appointmentService.save(date, time, notes, productId, employeeId, clientId);
    });
  }

  @Test
  void testSave_ShouldThrowResourceNotFoundException_WhenEmployeeNotFound() {
    // GIVEN
    // Configurar los mocks
    LocalDate date = LocalDate.now();
    LocalTime time = LocalTime.now();
    int productId = 1;
    int employeeId = 1;
    int clientId = 1;
    String notes = "Notas de prueba";

    Product product = new Product();
    when(productService.findById(productId)).thenReturn(Optional.of(product));
    when(employeeService.findById(employeeId)).thenReturn(Optional.empty());

    // WHEN THEN
    assertThrows(ResourceNotFoundException.class, () -> {
      appointmentService.save(date, time, notes, productId, employeeId, clientId);
    });
  }

  @Test
  void testSave_ShouldThrowResourceNotFoundException_WhenClientNotFound() {
    // GIVEN
    // Configurar los mocks
    LocalDate date = LocalDate.now();
    LocalTime time = LocalTime.now();
    int productId = 1;
    int employeeId = 1;
    int clientId = 1;
    String notes = "Notas de prueba";

    Product product = new Product();
    Employee employee = new Employee();
    when(productService.findById(productId)).thenReturn(Optional.of(product));
    when(employeeService.findById(employeeId)).thenReturn(Optional.of(employee));
    when(clientService.findById(clientId)).thenReturn(Optional.empty());

    // WHEN THEN
    assertThrows(ResourceNotFoundException.class, () -> {
      appointmentService.save(date, time, notes, productId, employeeId, clientId);
    });
  }

  @Test
  void testFindAll() {

    // GIVEN 
    int page = 0; // Página inicial
    int size = 10; // Cantidad de elementos por página

    Pageable pagination = PageRequest.of(page, size);

    List<Appointment> listAppointments = Arrays.asList(new Appointment(), new Appointment(), new Appointment());

    Page<Appointment> appointmentPage = new PageImpl<>(listAppointments, pagination, listAppointments.size());
    // Page<Appointment> appointmentPage = new PageImpl<>(listAppointments); // opción corta, pero cambia el size a 3

    // Mockear el comportamiento de respuesta del repositorio
    when(appointmentRepository.findAll(pagination)).thenReturn(appointmentPage);

    // WHEN  
    Page<Appointment> result = appointmentService.findAll(pagination);

    // THEN
    verify(appointmentRepository, times(1)).findAll(pagination);
    assertThat(result).isNotNull();
    assertThat(result.getContent()).isNotEmpty(); // Verificar que haya elementos en la página
    assertThat(result.getSize()).isEqualTo(size); // Verificar la cantidad de elementos por página
    assertThat(result.getNumber()).isEqualTo(page); // Verificar el número de página actual
    // Verificaciones adicionales (opcional):
    // assertThat(result.getContent().get(0).getId()).isEqualTo(appointment1.getId());
  }

  @Test
  void testFindById() {
    // GIVEN 
    int appointmentId = 1;
    Appointment appointment = new Appointment();
    appointment.setId(appointmentId);
    // se configura el mock appointmentRepository para que devuelva este objeto cuando se llame al método findById con el ID de prueba.
    when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

    // WHEN  
    Optional<Appointment> result = appointmentService.findById(appointmentId);

    // THEN  
    assertTrue(result.isPresent());
    assertEquals(appointment, result.get());
    verify(appointmentRepository, times(1)).findById(appointmentId);
  }

  @Test
  void testFindById_NonExistingAppointment() {
    // Datos de prueba
    int appointmentId = 1;

    // Configurar el mock
    when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

    // Ejecutar el método que se está probando
    Optional<Appointment> result = appointmentService.findById(appointmentId);

    // Verificar los resultados
    assertFalse(result.isPresent());
    verify(appointmentRepository, times(1)).findById(appointmentId);
  }

  @Test
  void testUpdate_ShouldReturnUpdatedAppointment() {
    // GIVEN
    int appointmentId = 1;
    LocalDate date = LocalDate.now();
    LocalTime time = LocalTime.now();
    int productId = 1;
    int employeeId = 1;
    String notes = "Notas actualizadas";

    Appointment appointment = new Appointment();
    appointment.setId(appointmentId);

    Product product = new Product();
    product.setId(1);
    Employee employee = new Employee();

    // Configurar los mocks
    when(appointmentRepository.getReferenceById(appointmentId)).thenReturn(appointment);
    when(productService.findById(productId)).thenReturn(Optional.of(product));
    when(employeeService.findById(employeeId)).thenReturn(Optional.of(employee));

    // Ejecutar el método que se está probando
    appointmentService.update(appointmentId, date, time, productId, employeeId, notes);

    // Verificar los resultados
    verify(appointmentRepository, times(1)).save(captor.capture());
    Appointment updatedAppointment = captor.getValue();
    verify(appointmentRepository, times(1)).getReferenceById(appointmentId);
    verify(productService, times(1)).findById(productId);
    verify(employeeService, times(1)).findById(employeeId);

    assertNotNull(updatedAppointment);
    assertThat(updatedAppointment)
        .hasFieldOrPropertyWithValue("date", date)
        .hasFieldOrPropertyWithValue("time", time);
    assertThat(updatedAppointment.getId()).isEqualTo(product.getId());
  }

  @Test
  void testUpdate_ThrowException_WhenProductNotFound() {
    // GIVEN
    int appointmentId = 1;
    LocalDate date = LocalDate.now();
    LocalTime time = LocalTime.now();
    int productId = 1;
    int employeeId = 1;
    String notes = "Notas actualizadas";

    Appointment appointment = new Appointment();
    when(appointmentRepository.getReferenceById(appointmentId)).thenReturn(appointment);
    when(productService.findById(productId)).thenReturn(Optional.empty());

    // WHEN & THEN
    assertThrows(ResourceNotFoundException.class,
        () -> appointmentService.update(appointmentId, date, time, productId, employeeId, notes));
  }

  @Test
  void testUpdate_ThrowException_WhenEmployeeNotFound() {
    // GIVEN
    int appointmentId = 1;
    LocalDate date = LocalDate.now();
    LocalTime time = LocalTime.now();
    int productId = 1;
    int employeeId = 1;
    String notes = "Notas actualizadas";

    Appointment appointment = new Appointment();
    Product product = new Product();

    when(appointmentRepository.getReferenceById(appointmentId)).thenReturn(appointment);
    when(productService.findById(productId)).thenReturn(Optional.of(product));
    when(employeeService.findById(employeeId)).thenReturn(Optional.empty());

    // WHEN & THEN
    assertThrows(ResourceNotFoundException.class,
        () -> appointmentService.update(appointmentId, date, time, productId, employeeId, notes));
  }

  @Test
  void testDeleteAppointment() {
    // Datos de prueba
    int appointmentId = 1;
    Appointment appointment = new Appointment();
    appointment.setId(appointmentId);
    appointment.setCondition("pending");
    appointment.setStatus(true);

    // Configurar el mock
    when(appointmentRepository.getReferenceById(appointmentId)).thenReturn(appointment);

    // Ejecutar el método que se está probando
    appointmentService.delete(appointmentId);

    // Verificar los resultados
    assertEquals("canceled", appointment.getCondition());
    assertFalse(appointment.isStatus());

    verify(appointmentRepository, times(1)).getReferenceById(appointmentId);
  }
}
