package com.unaux.dairo.api.controller;

import java.net.URI;
import java.time.LocalDate;
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

import com.unaux.dairo.api.domain.client.Client;
import com.unaux.dairo.api.domain.client.ClientCreateDto;
import com.unaux.dairo.api.domain.client.ClientFindDto;
import com.unaux.dairo.api.domain.client.ClientResponseDto;
import com.unaux.dairo.api.domain.client.ClientUpdateDto;
import com.unaux.dairo.api.infra.errors.ResourceNotFoundException;
import com.unaux.dairo.api.service.ClientService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/client")
// @PreAuthorize("hasRole('ADMIN')")
// @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class ClientController {
  // private static final Logger logger = LogManager.getLogger(ClientController.class);
  private final ClientService clientService;

  ClientController(ClientService clientService) {
    this.clientService = clientService;
  }

  @PostMapping
  public ResponseEntity<?> createClient(UriComponentsBuilder uriComponentsBuilder,
      @RequestBody @Valid ClientCreateDto clientCreateDto) {
    // Extraer datos
    LocalDate birthday = clientCreateDto.birthday();
    String lastName = clientCreateDto.lastName();
    String name = clientCreateDto.name();
    String phone = clientCreateDto.phone();
    int userId = clientCreateDto.userId();

    try {
      // creamos el client
      Client client = clientService.save(birthday, lastName, name, phone, userId);
      // Creamos un DTO para retornar el objeto al frontend
      ClientResponseDto response = mapClientToResponseDto(client);
      // Aquí crearemos una url que corresponde al objeto que se creó en la base de datos.
      URI url = uriComponentsBuilder
          .path("api/v1/client/{id}")
          .buildAndExpand(client.getId())
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
  public ResponseEntity<Page<ClientFindDto>> listAllClient(Pageable pagination) {
    Page<Client> listClients = clientService.findAll(pagination);
    return ResponseEntity.ok(listClients.map(ClientFindDto::new));
  }

  @GetMapping("/enabled")
  public ResponseEntity<Page<ClientFindDto>> listEnabledStatusClient(Pageable pagination) {
    Page<Client> ListClients = clientService.findEnabled(pagination);
    return ResponseEntity.ok(ListClients.map(ClientFindDto::new));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> findClient(@PathVariable int id) {
    // *** No hay validaciones menores para realizar
    Optional<Client> clientOptional = clientService.findById(id);

    if (clientOptional.isEmpty()) {

      Map<String, Object> errorDetails = new HashMap<>();
      errorDetails.put("code", "RESOURCE_NOT_FOUND");
      errorDetails.put("message", "The requested resource was not found");
      errorDetails.put("details", "No record with the ID " + id + " was found in the database.");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);

      // Enviar un código de estado HTTP 404 Not Found con un mensaje en el cuerpo
      //! return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The requested resource was not found");
    }
    // Creamos un DTO para retornar el objeto al frontend
    Client client = clientOptional.get();
    ClientResponseDto response = mapClientToResponseDto(client);

    return ResponseEntity.ok(response);
  }

  @PutMapping
  public ResponseEntity<?> updateClient(@RequestBody @Valid ClientUpdateDto clientUpdateDto) {
    // Extraer datos
    int id = clientUpdateDto.id();
    LocalDate birthday = clientUpdateDto.birthday();
    String lastName = clientUpdateDto.lastName();
    String name = clientUpdateDto.name();
    String phone = clientUpdateDto.phone();

    // *** validaciones menores (tipo, formato, etc)
    if (isValidBirthday(birthday)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error in Birthday");
    }

    try {
      Client client = clientService.update(id, birthday, lastName, name, phone);
      // Creamos un DTO para retornar el objeto al frontend
      ClientResponseDto response = mapClientToResponseDto(client);

      return ResponseEntity.ok(response);
    } catch (EntityNotFoundException e) {
      String errorMessage = "Resource not found with ID: %d".formatted(clientUpdateDto.id());
      return ResponseEntity.badRequest().body(errorMessage);
    }
  }

  @DeleteMapping("/{id}")
  @Transactional
  public ResponseEntity<?> deleteClient(@PathVariable int id) {
    try {
      clientService.delete(id);
      // Retornamos una respuesta vacía
      return ResponseEntity.noContent().build();
    } catch (EntityNotFoundException e) {
      String errorMessage = "Resource not found with ID: %d".formatted(id);
      return ResponseEntity.badRequest().body(errorMessage);
    }
  }

  private boolean isValidBirthday(LocalDate birthday) {
    int year = birthday.getYear();

    return (year < 1924 || birthday.isAfter(LocalDate.now()));
  }

  private ClientResponseDto mapClientToResponseDto(Client client) {
    return new ClientResponseDto(
        client.getId(),
        client.getBirthday(),
        client.getLastName(),
        client.getName(),
        client.getPhone(),
        client.isStatus(),
        client.getType(),
        client.getUser().getId());
  }
}
