package com.unaux.dairo.api.controller;

import com.unaux.dairo.api.domain.client.*;
import com.unaux.dairo.api.repository.ClientRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("api/client")
public class ClientController {

  private final ClientRepository clientRepository;

  ClientController(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
  }

  @PostMapping
  public ResponseEntity createClient(
    @RequestBody @Valid ClientCreateDto clientCreateDto,
    UriComponentsBuilder uriComponentsBuilder
  ) {
    if (!clientCreateDto.password().equals(clientCreateDto.confirmPassword())) {
      return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .header("Error-Password", "password and confirmPassword do not match")
        .build();
    }
    Client client = clientRepository.save(new Client(clientCreateDto));
    ClientResponseDto response = new ClientResponseDto(
      client.getId(),
      client.getName(),
      client.getLastName(),
      client.getPhone(),
      client.getBirthday(),
      client.getEmail(),
      client.getType()
    );
    URI url = uriComponentsBuilder
      .path("/client/{id}")
      .buildAndExpand(client.getId())
      .toUri();
    return ResponseEntity.created(url).body(response);
  }

  @GetMapping
  public ResponseEntity<Page<ClientFindDto>> listClient(Pageable paginacion) {
    //return clientRepository.findAll(paginacion).map(ClientFindDto::new);
    return ResponseEntity.ok(
      clientRepository.findByStatusTrue(paginacion).map(ClientFindDto::new)
    );
  }

  @GetMapping("/{id}")
  public ResponseEntity<ClientResponseDto> findClient(@PathVariable int id) {
    Client client = clientRepository.getReferenceById(id);
    ClientResponseDto response = new ClientResponseDto(
      client.getId(),
      client.getName(),
      client.getLastName(),
      client.getPhone(),
      client.getBirthday(),
      client.getEmail(),
      client.getType()
    );
    return ResponseEntity.ok(response);
  }

  @PutMapping
  @Transactional
  public ResponseEntity<ClientResponseDto> updateClient(
    @RequestBody @Valid ClientUpdateDto clientUpdateDto
  ) {
    // con el ID recibido buscamos en DB y almacenamos los datos existentes en client
    Client client = clientRepository.getReferenceById(clientUpdateDto.id());

    // verificamos que la contraseña coincida con la guardada en la base de datos
    if (!client.matchesPassword(clientUpdateDto.password(), client.getPassword())) {
      return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .header("Error-Password", "current password does not match")
        .build();
    }

    // si el cliente quiere actualizar su contraseña
    if (clientUpdateDto.newPassword() != null) {
      // verificamos que newPassword no sea igual que la contraseña actual
      if (clientUpdateDto.newPassword().equals(clientUpdateDto.password())) {
        return ResponseEntity
          .status(HttpStatus.NOT_FOUND)
          .header("Error-Password", "Password and newPassword are equals")
          .build();
      }

      // verificamos que newPassword y confirmPassword no sea vacíos o null
      if (
        clientUpdateDto.newPassword() == "" ||
        clientUpdateDto.confirmPassword() == "" ||
        clientUpdateDto.newPassword() == null ||
        clientUpdateDto.confirmPassword() == null
      ) {
        return ResponseEntity
          .status(HttpStatus.NOT_FOUND)
          .header(
            "Error-Password",
            "newPassword or confirmPassword is empty or null"
          )
          .build();
      }

      // verificamos que newPassword y confirmPassword sean iguales
      if (
        !clientUpdateDto.newPassword().equals(clientUpdateDto.confirmPassword())
      ) {
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
      client.getName(),
      client.getLastName(),
      client.getPhone(),
      client.getBirthday(),
      client.getEmail(),
      client.getType()
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

  @DeleteMapping("/{id}") // Delete lógico
  @Transactional
  public ResponseEntity deleteClient(@PathVariable int id) {
    Client client = clientRepository.getReferenceById(id);
    client.inactivate();
    return ResponseEntity.noContent().build();
  }
}
