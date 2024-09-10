package com.unaux.dairo.api.service;

import java.time.LocalDate;
import java.util.Optional;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.unaux.dairo.api.domain.client.Client;
import com.unaux.dairo.api.domain.user.User;
import com.unaux.dairo.api.infra.errors.ResourceNotFoundException;
import com.unaux.dairo.api.repository.ClientRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClientService {

  // private static final Logger logger = LoggerFactory.getLogger(ClientService.class);
  private final ClientRepository clientRepository;
  private final UserService userService;

  public ClientService(ClientRepository clientRepository, UserService userService) {
    this.clientRepository = clientRepository;
    this.userService = userService;
  }

  public Client save(LocalDate birthday, String lastName, String name, String phone, int userId) {

    //Buscamos el User
    User user = userService.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    // creamos el Client
    Client client = new Client(birthday, lastName, name, phone, user);
    return clientRepository.save(client);
  }

  public Page<Client> findAll(Pageable pagination) {
    return clientRepository.findAll(pagination);
  }

  public Page<Client> findEnabled(Pageable pagination) {
    return clientRepository.findByStatusTrue(pagination);
  }

  public Optional<Client> findById(int id) {
    return clientRepository.findById(id);
  }

  public Client update(int id, LocalDate birthday, String lastName, String name, String phone) {
    // *** validamos que no se pueda modificar el ID 1, ya que es el admin del sistema
    if (id == 1) {
      throw new EntityNotFoundException();
    }
    // con el ID Buscamos la Entidad a actualizar
    Client client = clientRepository.getReferenceById(id);
    // logger.info("Valor de user antes de actualizar:");
    // logger.info("ID: {}", client.getId());
    // Actualizamos la Entidad con los datos del DTO
    client.update(birthday, lastName, name, phone);
    // retornamos la Entidad ya actualizada
    return clientRepository.save(client);
  }

  public void delete(int id) {
    // con el ID Buscamos la Entidad para desactivar
    Client client = clientRepository.getReferenceById(id);
    // Desactivamos - borrado lógico
    client.inactivate();
  }
}
