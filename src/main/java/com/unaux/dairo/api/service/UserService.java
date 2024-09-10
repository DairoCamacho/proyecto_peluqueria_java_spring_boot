package com.unaux.dairo.api.service;

import java.util.Optional;

// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.unaux.dairo.api.domain.user.User;
import com.unaux.dairo.api.infra.errors.EmailAlreadyExistsException;
import com.unaux.dairo.api.infra.errors.PasswordsDoNotMatchException;
import com.unaux.dairo.api.infra.errors.ResourceNotFoundException;
import com.unaux.dairo.api.repository.UserRepository;

@Service
public class UserService {

  // private static final Logger logger = LogManager.getLogger(UserService.class);
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User save(String email, String password) {
    // Validamos si el email no existe en DB
    if (existsByEmail(email)) {
      throw new EmailAlreadyExistsException("The email is already registered");
    }
    User user = new User(email, password);
    return userRepository.save(user);
  }

  public Page<User> findAll(Pageable pagination) {
    return userRepository.findAll(pagination);
  }

  public Page<User> findEnabled(Pageable pagination) {
    return userRepository.findByStatusTrue(pagination);
  }

  public Optional<User> findById(int id) {
    return userRepository.findById(id);
  }

  public User update(int id, String email, String password, String newPassword) {
    //! NO PERMITE ACTUALIZAR ROL o STATUS
    // *** validamos que no se pueda modificar el ID 1, ya que es el admin del sistema
    if (id == 1) {
      throw new ResourceNotFoundException("ID not found");
    }
    // Validamos si el email a actualizar no existe en DB
    if (email != null && existsByEmail(email)) {
      throw new EmailAlreadyExistsException("The email " + email + " is already registered");
    }
    // con el ID Buscamos la Entidad a actualizar
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    // logger.debug("Valor de user antes de actualizar: {}", user.toString());
    // extraemos el password existente en DB
    String existingPassword = user.getPassword();
    // verificamos que la contraseña enviada coincida con la guardada en DB
    if (!matchesPassword(password, existingPassword)) {
      throw new PasswordsDoNotMatchException("Passwords do not match");
    }
    // Actualizamos la Entidad con los datos del DTO
    user.update(email, newPassword);
    // retornamos la Entidad ya actualizada
    // logger.debug("Valor de user después de actualizar: {}", user.toString());

    return userRepository.save(user);
  }

  public void delete(int id) {
    // con el ID Buscamos la Entidad para desactivar
    User user = userRepository.getReferenceById(id);
    // Desactivamos - borrado lógico
    user.inactivate();
  }

  public boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  boolean matchesPassword(String rawPassword, String encodedPassword) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    return encoder.matches(rawPassword, encodedPassword);
  }
}
