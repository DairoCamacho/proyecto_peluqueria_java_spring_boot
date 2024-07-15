package com.unaux.dairo.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import com.unaux.dairo.api.domain.appointment.Appointment;
import com.unaux.dairo.api.domain.appointment.AppointmentCreateDto;
import com.unaux.dairo.api.domain.appointment.AppointmentFindDto;
import com.unaux.dairo.api.domain.appointment.AppointmentResponseDto;
import com.unaux.dairo.api.domain.appointment.AppointmentUpdateDto;
import com.unaux.dairo.api.domain.client.Client;
import com.unaux.dairo.api.domain.employee.Employee;
import com.unaux.dairo.api.domain.product.Product;
import com.unaux.dairo.api.infra.errors.ResourceNotFoundException;
import com.unaux.dairo.api.service.AppointmentService;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class AppointmentControllerTest {

        @Mock
        private AppointmentService appointmentService;

        @InjectMocks
        private AppointmentController appointmentController;
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
        /* 
        private User user1;
        private Client client1;
        private HairSalon hairSalon1;
        private Employee employee1;
        private WorkingHours workingHours1;
        private Product product1; 
        */
        private Appointment appointment1;

        @BeforeEach
        public void setUp() {
                // Creamos un cliente para las pruebas
                Client client1 = Client.builder().id(1).build();

                // Creamos un Empleado para las pruebas
                Employee employee1 = Employee.builder().id(1).build();

                // Creamos un Servicio para las pruebas
                Product product1 = Product.builder().id(1).build();

                // Creamos una Cita para las pruebas
                appointment1 = Appointment.builder()
                                .id(1)
                                .date(LocalDate.parse("2024-11-11"))
                                .time(LocalTime.parse("00:30"))
                                .condition("pending")
                                .notes("quiero un corte de pelo en capas")
                                .product(product1)
                                .employee(employee1)
                                .client(client1)
                                .status(true)
                                .build();
        }

        @Test
        void createAppointment_ShouldReturnCreatedAppointment() {
                // GIVEN
                // Crear una instancia de UriComponentsBuilder
                UriComponentsBuilder uri = UriComponentsBuilder.newInstance();

                AppointmentCreateDto appointmentCreateDto = new AppointmentCreateDto(
                                LocalDate.now().plusDays(1),
                                LocalTime.now(),
                                "Test notes",
                                1,
                                1,
                                1);

                when(appointmentService
                                .save(any(), any(), anyString(), anyInt(), anyInt(), anyInt()))
                                .thenReturn(appointment1);

                // WHEN
                ResponseEntity<?> response = appointmentController.createAppointment(uri, appointmentCreateDto);

                // THEN
                assertEquals(HttpStatus.CREATED, response.getStatusCode());
                assertDoesNotThrow(() -> response.getBody()); // Verificar que no se haya lanzado ninguna excepción
                assertNotNull(response.getBody()); // Verificar que la respuesta contiene datos válidos del objeto Appointment
                assertNotNull(response.getHeaders().getLocation()); // Verificar que la URL de la ubicación creada sea válida
        }

        @Test
        void createAppointment_WithPreviousDate() {
                // Creamos el DTO
                AppointmentCreateDto appointmentCreateDto = new AppointmentCreateDto(
                                LocalDate.now(),
                                LocalTime.now().minusMinutes(1),
                                "Test notes",
                                1,
                                1,
                                1);

                // WHEN
                ResponseEntity<?> response = appointmentController.createAppointment(null, appointmentCreateDto);

                // THEN
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                Map<String, Object> errorDetails = (Map<String, Object>) response.getBody();
                assertNotNull(errorDetails);
                assertEquals("Error in the request", errorDetails.get("message"));
                assertEquals("The date and time is earlier than the current date.", errorDetails.get("details"));
        }

        @Test
        void createAppointment_ShouldThrowResourceNotFoundException() {
                // GIVEN
                UriComponentsBuilder uri = UriComponentsBuilder.newInstance();
                AppointmentCreateDto appointmentCreateDto = new AppointmentCreateDto(
                                LocalDate.now().plusDays(1),
                                LocalTime.now(),
                                "Test notes",
                                1,
                                1,
                                1);
                // Configura los mocks para que devuelvan Optional.empty()
                when(appointmentService.save(any(), any(), any(), anyInt(), anyInt(), anyInt()))
                                .thenThrow(new ResourceNotFoundException("Test exception message"));
                // WHEN 
                ResponseEntity<?> response = appointmentController.createAppointment(null, appointmentCreateDto);
                // THEN
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals("Test exception message", response.getBody());
        }

        @Test
        void listAllAppointment_ShouldReturnAppointmentPage() {
                // GIVEN
                int page = 0; // Página inicial
                int size = 10; // Cantidad de elementos por página
                Pageable pagination = PageRequest.of(page, size);
                List<Appointment> listAppointments = Arrays.asList(appointment1);
                Page<Appointment> appointmentPage = new PageImpl<>(listAppointments, pagination,
                                listAppointments.size());
                // Page<Appointment> appointmentPage = new PageImpl<>(listAppointments); // opción corta, pero cambia el size a 3

                when(appointmentService.findAll(pagination)).thenReturn(appointmentPage);

                // WHEN
                ResponseEntity<Page<AppointmentFindDto>> response = appointmentController
                                .listAllAppointment(pagination);

                // THEN
                assertNotNull(response.getBody());
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertEquals(size, response.getBody().getSize());
                assertEquals(page, response.getBody().getNumber());
                // Obtener el AppointmentFindDto en la posición 0
                AppointmentFindDto firstAppointmentDto = response.getBody().getContent().get(0);
                // Comparar el ID del Appointment en la posición 0
                assertEquals(appointment1.getId(), firstAppointmentDto.id());
        }

        @Test
        void findAppointment_ShouldReturnAppointment() {
                // GIVEN
                int id = 1;

                when(appointmentService.findById(id)).thenReturn(Optional.of(appointment1));

                // WHEN
                ResponseEntity<?> response = appointmentController.findAppointment(id);

                // THEN
                assertEquals(HttpStatus.OK, response.getStatusCode());
                // Obtener el AppointmentFindDto de la respuesta
                AppointmentResponseDto appointmentDto = (AppointmentResponseDto) response.getBody();
                assertEquals(appointment1.getId(), appointmentDto.id());

        }

        @Test
        void findAppointment_ShouldReturnNotFound() {
                // GIVEN
                int id = 1;
                when(appointmentService.findById(id)).thenReturn(Optional.empty());

                // WHEN
                ResponseEntity<?> response = appointmentController.findAppointment(id);

                // THEN
                assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        void updateAppointment_ShouldupdateAppointment() {
                // GIVEN
                AppointmentUpdateDto appointmentUpdateDto = new AppointmentUpdateDto(
                                1,
                                LocalDate.now().plusDays(1),
                                LocalTime.now(),
                                2,
                                3,
                                "Test notes");

                when(appointmentService.update(anyInt(), any(), any(), anyInt(), anyInt(), anyString()))
                                .thenReturn(appointment1);

                // WHEN
                ResponseEntity<?> response = appointmentController.updateAppointment(appointmentUpdateDto);

                // THEN
                assertNotNull(response);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                // Obtener el AppointmentFindDto de la respuesta
                AppointmentResponseDto appointmentDto = (AppointmentResponseDto) response.getBody();
                assertEquals(appointment1.getId(), appointmentDto.id());
        }

        @Test
        void updateAppointment_WithPreviousDate() {
                // GIVEN
                AppointmentUpdateDto appointmentUpdateDto = new AppointmentUpdateDto(
                                1,
                                LocalDate.now(),
                                LocalTime.now().minusMinutes(1),
                                2,
                                3,
                                "Test notes");
                // WHEN
                ResponseEntity<?> response = appointmentController.updateAppointment(appointmentUpdateDto);
                // THEN
                assertThat(response).isNotNull();
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                Map<String, String> body = (Map<String, String>) response.getBody();
                assertEquals("Error in the request", body.get("message"));
                assertEquals("The date and time is earlier than the current date.", body.get("details"));
        }

        @Test
        void updateAppointment_ShouldThrowEntityNotFoundException() {
                // GIVEN
                AppointmentUpdateDto appointmentUpdateDto = new AppointmentUpdateDto(
                                1,
                                LocalDate.now(),
                                LocalTime.now().plusMinutes(1),
                                2,
                                3,
                                "Test notes");

                when(appointmentService.update(anyInt(), any(), any(), anyInt(), anyInt(), anyString()))
                                .thenThrow(new EntityNotFoundException());

                // WHEN
                ResponseEntity<?> response = appointmentController.updateAppointment(appointmentUpdateDto);
                // THEN
                assertThat(response).isNotNull();
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals("Resource not found with ID: 1", response.getBody());
        }

        @Test
        void updateAppointment_ShouldThrowResourceNotFoundException() {
                // GIVEN
                AppointmentUpdateDto appointmentUpdateDto = new AppointmentUpdateDto(
                                1,
                                LocalDate.now(),
                                LocalTime.now().plusMinutes(1),
                                2,
                                3,
                                "Test notes");

                when(appointmentService.update(anyInt(), any(), any(), anyInt(), anyInt(), anyString()))
                                .thenThrow(new ResourceNotFoundException("Test exception message"));

                // WHEN
                ResponseEntity<?> response = appointmentController.updateAppointment(appointmentUpdateDto);
                // THEN
                assertThat(response).isNotNull();
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals("Test exception message", response.getBody());
        }

        @Test
        void deleteAppointment_ShouldDeleteAppointment() {
                // GIVEN
                int id = 1;
                doNothing().when(appointmentService).delete(id);

                // WHEN
                ResponseEntity<?> response = appointmentController.deleteAppointment(id);

                // THEN
                assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        }

        @Test
        void deleteAppointment_ShouldThrowEntityNotFoundException() {
                // GIVEN
                int id = 1;
                // Mock del comportamiento de appointmentService.delete para lanzar una excepción EntityNotFoundException.
                doThrow(new EntityNotFoundException("Resource not found with ID: " + id)).when(appointmentService)
                                .delete(id);
                /*
                 * NOTE:Cuando se trabaja con métodos que no devuelven valores (métodos void) en Mockito, 
                 * se debe utilizar el método doThrow en lugar de when. 
                 * El método when se utiliza para configurar el comportamiento del método simulado y 
                 * devolver un valor específico, pero no funciona para métodos void.
                 */

                // WHEN
                ResponseEntity<?> response = appointmentController.deleteAppointment(id);

                // THEN
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals("Resource not found with ID: " + id, response.getBody());
        }

}