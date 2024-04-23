package com.unaux.dairo.api.controller;

import com.unaux.dairo.api.domain.user.User;
import com.unaux.dairo.api.domain.user.UserCreateDto;
import com.unaux.dairo.api.domain.user.UserFindDto;
import com.unaux.dairo.api.domain.user.UserResponseDto;
import com.unaux.dairo.api.domain.user.UserUpdateDto;
import com.unaux.dairo.api.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
public class UserController {

  private final UserRepository userRepository;

  UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  // se usa dentro de clientController
  @PostMapping
  public User createUser(User user) {
    return userRepository.save(user);
  }

  // se usa dentro de clientController
  @GetMapping("/email")
  public boolean findUserByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  // se usa dentro de Put de este controlador
  public boolean matchesPassword(String rawPassword, String encodedPassword) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    return encoder.matches(rawPassword, encodedPassword);
  }

  // CONSULTAR TODOS LOS USUARIOS
  @GetMapping("/all")
  public List<User> listAllUsers() {
    return userRepository.findAll();
  }

  // CONSULTAR TODOS LOS USUARIOS HÁBILES
  @GetMapping("/enabled")
  public ResponseEntity<Page<UserFindDto>> listEnabledUsers(
    Pageable pagination
  ) {
    return ResponseEntity.ok(
      userRepository.findByStatusTrue(pagination).map(UserFindDto::new)
    );
  }

  // CONSULTAR USUARIO POR ID
  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDto> findUser(@PathVariable int id) {
    // con el id buscamos en DB y guardamos dentro de "user"
    User user = userRepository.getReferenceById(id);

    // para no devolver todos los datos, usaremos el DTO
    UserResponseDto response = new UserResponseDto(
      user.getId(),
      user.getEmail(),
      user.isStatus(),
      user.getRole()
    );
    return ResponseEntity.ok(response);
  }

  /* 
  // Otras formas de hacer la consulta
  @GetMapping("/{id}")
  public ResponseEntity<User> findUser(@PathVariable int id) {
    Optional<User> user = userRepository.findById(id);
    return user
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/{id}")
  public User findUser(@PathVariable int id) {
    return userRepository.getReferenceById(id);
  }
 */

  @PutMapping("/{id}")
  public ResponseEntity<UserResponseDto> updateUser(
    @RequestBody UserUpdateDto userUpdateDto
  ) {
    // extraemos los datos recibidos
    int id = userUpdateDto.id();
    String password = userUpdateDto.password();
    String newPassword = userUpdateDto.newPassword();
    String confirmPassword = userUpdateDto.confirmPassword();

    // con el ID recibido buscamos en DB y almacenamos en "user"
    User user = userRepository.getReferenceById(id);
    // extraemos el password existente en DB
    String existingPassword = user.getPassword();

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

    user.update(userUpdateDto);

    UserResponseDto response = new UserResponseDto(
      user.getId(),
      user.getEmail(),
      user.isStatus(),
      user.getRole()
    );

    return ResponseEntity.ok(response);
  }


  /*
  // option 2
  @PutMapping("/{id}")
  public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User user) {
      Optional<User> existingUser = userRepository.findById(id);
      if (existingUser.isPresent()) {
          user.setId(id);
          User updatedUser = userRepository.save(user);
          return ResponseEntity.ok(updatedUser);
      } else {
          return ResponseEntity.notFound().build();
      }
  }

  // option 3
  @PutMapping("/{id}")
  public User updateUser(int id, User user) {
    User existingUser = userRepository.getReferenceById(id);
    existingUser.setEmail(user.getEmail());
    existingUser.setPassword(user.getPassword());
    existingUser.setStatus(user.isStatus());
    return userRepository.save(existingUser);
  }
 */

  @DeleteMapping("/{id}")
  public ResponseEntity deleteUser(@PathVariable int id) {
    User user = userRepository.getReferenceById(id);
    user.inactivate();
    return ResponseEntity.noContent().build();
  }
}
