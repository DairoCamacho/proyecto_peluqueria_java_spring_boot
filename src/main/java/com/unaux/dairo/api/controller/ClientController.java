package com.unaux.dairo.api.controller;

import com.unaux.dairo.api.domain.client.*;
import com.unaux.dairo.api.domain.user.User;
import com.unaux.dairo.api.repository.ClientRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("api/client")
public class ClientController {

  private final ClientRepository clientRepository;
  private final UserController userController;

  ClientController(
    ClientRepository clientRepository,
    UserController userController
  ) {
    this.clientRepository = clientRepository;
    this.userController = userController;
  }

  @PostMapping
  @Transactional
  public ResponseEntity createClient(
    @RequestBody @Valid ClientCreateDto clientCreateDto,
    UriComponentsBuilder uriComponentsBuilder
  ) {
    // extraemos los datos del DTO
    LocalDate birthday = clientCreateDto.birthday();
    String confirmPassword = clientCreateDto.confirmPassword();
    String email = clientCreateDto.email();
    String lastName = clientCreateDto.lastName();
    String name = clientCreateDto.name();
    String password = clientCreateDto.password();
    String phone = clientCreateDto.phone();

    // verificación que el email no exista previamente en la DB
    if (userController.findUserByEmail(email)) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .header("Error-Email", "The email already exists in the database")
        .build();
    }

    // verificación de los dos password que se reciben del frontend
    if (!password.equals(confirmPassword)) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .header("Error-Password", "password and confirmPassword do not match")
        .build();
    }

    // creamos la persona
    Client client = clientRepository.save(
      new Client(birthday, lastName, name, phone)
    );

    // creamos el usuario
    User user = userController.createUser(new User(client, email, password));

    ClientResponseDto response = new ClientResponseDto(
      client.getId(),
      client.getBirthday(),
      client.getLastName(),
      client.getName(),
      client.getPhone(),
      client.getType(),
      user.getEmail()
    );

    URI url = uriComponentsBuilder
      .path("api/client/{id}")
      .buildAndExpand(client.getId())
      .toUri();
    return ResponseEntity.created(url).body(response);
  }

  @GetMapping
  public ResponseEntity<Page<ClientFindDto>> listAllClient(
    Pageable pagination
  ) {
    return ResponseEntity.ok(
      clientRepository.findAll(pagination).map(ClientFindDto::new)
    );
  }

  @GetMapping("/enabled")
  public ResponseEntity<Page<ClientFindDto>> listEnabledClient(
    Pageable pagination
  ) {
    // return clientRepository.findAll(pagination).map(ClientFindDto::new);
    return ResponseEntity.ok(
      clientRepository
        .findClientsWithStatusUser(pagination)
        .map(ClientFindDto::new)
    );
  }

  @GetMapping("/{id}")
  public ResponseEntity findClient(@PathVariable int id) {
    Optional<Client> clientOptional = clientRepository.findById(id);

    if (clientOptional.isPresent()) {
      Client client = clientOptional.get();
      ClientResponseDto response = new ClientResponseDto(
        client.getId(),
        client.getBirthday(),
        client.getLastName(),
        client.getName(),
        client.getPhone(),
        client.getType(),
        client.getUser().getEmail()
      );

      return ResponseEntity.ok(response);
    } else {
      // Enviar un código de estado HTTP apropiado, como 404 Not Found
      return ResponseEntity.notFound().build();
      // Enviar un código de estado HTTP 404 Not Found con un mensaje en el cuerpo
      // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El recurso solicitado no se encontró");
    }
  }

  /*
  // Otras formas de hacer la consulta

  @GetMapping("/{id}")
  public ResponseEntity<ClientResponseDto> findClient(@PathVariable int id) {
    // con el id buscamos en DB y guardamos dentro de "user"
    Client client = clientRepository.findById(id).orElseThrow(() -> new RuntimeException("Client not found"));
    // Client client = clientRepository.getReferenceById(id);

    ClientResponseDto response = new ClientResponseDto(
      client.getId(),
      client.getBirthday(),
      client.getLastName(),
      client.getName(),
      client.getPhone(),
      client.getType(),
      client.getUser().getEmail()
    );
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public Client findClient(@PathVariable int id) {
    return clientRepository.getReferenceById(id);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Client> findClient(@PathVariable int id) {
    Optional<Client> client = clientRepository.findById(id);
    return client.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }
   */

  @PutMapping
  @Transactional
  public ResponseEntity<ClientResponseDto> updateClient(
    @RequestBody @Valid ClientUpdateDto clientUpdateDto
  ) {
    // extraemos algunos datos del DTO
    int id = clientUpdateDto.id();
    String confirmPassword = clientUpdateDto.confirmPassword();
    String newPassword = clientUpdateDto.newPassword();
    String password = clientUpdateDto.password();

    // con el ID recibido buscamos en DB y almacenamos en "client"
    Client client = clientRepository.getReferenceById(id);
    // extraemos el password existente en DB
    String existingPassword = client.getUser().getPassword();

    // verificamos que la contraseña coincida con la guardada en DB
    if (!matchesPassword(password, existingPassword)) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .header("Error-Password", "current password does not match")
        .build();
    }

    // Para actualizar contraseña validar las variables necesarias
    if (newPassword != "" && newPassword != null) {
      // verificamos que newPassword no sea igual que la contraseña actual
      if (newPassword.equals(password)) {
        return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .header("Error-Password", "Password and newPassword are equals")
          .build();
      }

      // verificamos que confirmPassword no sea vacíos o null
      if (confirmPassword == "" || confirmPassword == null) {
        return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .header("Error-Password", "confirmPassword is empty or null")
          .build();
      }

      // verificamos que newPassword y confirmPassword sean iguales
      if (!newPassword.equals(confirmPassword)) {
        return ResponseEntity
          .status(HttpStatus.NOT_FOUND)
          .header(
            "Error-Password",
            "newPassword and confirmPassword do not match"
          )
          .build();
      }
    }

    client.update(clientUpdateDto);

    ClientResponseDto response = new ClientResponseDto(
      client.getId(),
      client.getBirthday(),
      client.getLastName(),
      client.getName(),
      client.getPhone(),
      client.getType(),
      client.getUser().getEmail()
    );

    return ResponseEntity.ok(response);
  }

  /* 
    // OJO: delete físico
    @DeleteMapping("/{id}")
    @Transactional
    public void deleteClient (@PathVariable int id){
    Client client = clientRepository.getReferenceById(id);
    clientRepository.delete(client);
    }
   */

  boolean matchesPassword(String rawPassword, String encodedPassword) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    return encoder.matches(rawPassword, encodedPassword);
  }
}
