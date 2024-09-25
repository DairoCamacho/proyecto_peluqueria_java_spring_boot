package com.unaux.dairo.api.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

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

import com.unaux.dairo.api.domain.user.User;
import com.unaux.dairo.api.domain.user.UserCreateDto;
import com.unaux.dairo.api.domain.user.UserFindDto;
import com.unaux.dairo.api.domain.user.UserResponseDto;
import com.unaux.dairo.api.domain.user.UserUpdateDto;
import com.unaux.dairo.api.infra.errors.EmailAlreadyExistsException;
import com.unaux.dairo.api.infra.errors.PasswordsDoNotMatchException;
import com.unaux.dairo.api.infra.errors.ResourceNotFoundException;
import com.unaux.dairo.api.service.UserService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("api/v1/user")
@Tag(name = "2. User", description = "Controller for User management")
// @PreAuthorize("hasRole('ADMIN')")
// @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class UserController {

  private final UserService userService;

  UserController(UserService userService) {
    this.userService = userService;
  }

  // se usa dentro de clientController
  @PostMapping
  @Operation(
    summary = "Create User",
    description = "Create a new user",
    tags = {"User"},
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "User creation data",
      required = true,
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = UserCreateDto.class)
      )
    ),
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "User created successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = UserResponseDto.class),
          examples = @ExampleObject(
            name = "example",
            value = "{\"id\": 1, \"email\": \"user@example.com\", \"role\": \"USER\", \"status\": true}"
          )
        )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Invalid input or email already exists",
        content = @Content(
          mediaType = "application/json",
          examples = @ExampleObject(
            name = "error",
            value = "{\"message\": \"Invalid input or email already exists\"}"
          )
        )
      )
    }
  )
  public ResponseEntity<?> createUser(@RequestBody @Valid UserCreateDto userCreateDto) {
    // Extraer los datos
    String email = userCreateDto.email();
    String password = userCreateDto.password();
    String confirmPassword = userCreateDto.confirmPassword();

    // *** validaciones menores (tipo, formato, etc)
    Map<String, Object> errors = new HashMap<>();
    // verificación que el password cumpla con los requisitos
    errors = validateEmailRequirements(errors, email);
    // verificación que el password cumpla con los requisitos de complejidad
    errors = validatePasswordRequirements(errors, password);
    if (!errors.isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("errors", errors));
    }

    // verificación de los dos password que se reciben del frontend
    if (!password.equals(confirmPassword)) {
      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .header("Error-Password", "password and confirmPassword do not match")
          .body("password and confirmPassword do not match"); // se puede remplazar por .build();
    }

    try {
      User user = userService.save(email, password);
      // Creamos un DTO para retornar el objeto al frontend
      UserResponseDto response = mapUserToResponseDto(user);

      return ResponseEntity.ok(response);
    } catch (EmailAlreadyExistsException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping
  @Operation(
    summary = "List All Users",
    description = "Retrieve a paginated list of all users",
    tags = {"User"},
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "List of users retrieved successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = Page.class),
          examples = @ExampleObject(
            name = "example",
            value = "{\"content\": [{\"id\": 1, \"email\": \"user@example.com\", \"role\": \"USER\", \"status\": true}], \"pageable\": \"INSTANCE\", \"totalPages\": 1, \"totalElements\": 1}"
          )
        )
      )
    }
  )
  public ResponseEntity<Page<UserFindDto>> listAllUsers(Pageable pagination) {
    Page<User> listUsers = userService.findAll(pagination);
    return ResponseEntity.ok(listUsers.map(UserFindDto::new));
  }

  @GetMapping("/enabled")
  @Operation(
    summary = "List Enabled Users",
    description = "Retrieve a paginated list of enabled users",
    tags = {"User"},
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "List of enabled users retrieved successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = Page.class),
          examples = @ExampleObject(
            name = "example",
            value = "{\"content\": [{\"id\": 1, \"email\": \"user@example.com\", \"role\": \"USER\", \"status\": true}], \"pageable\": \"INSTANCE\", \"totalPages\": 1, \"totalElements\": 1}"
          )
        )
      )
    }
  )
  public ResponseEntity<Page<UserFindDto>> listEnabledStatusUsers(Pageable pagination) {
    Page<User> listUsers = userService.findEnabled(pagination);
    return ResponseEntity.ok(listUsers.map(UserFindDto::new));
  }

  @GetMapping("/{id}")
  @Operation(
    summary = "Find User by ID",
    description = "Retrieve a user by their ID",
    tags = {"User"},
    parameters = @io.swagger.v3.oas.annotations.Parameter(
      name = "id",
      description = "ID of the user to be retrieved",
      required = true,
      example = "1"
    ),
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "User retrieved successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = UserResponseDto.class),
          examples = @ExampleObject(
            name = "example",
            value = "{\"id\": 1, \"email\": \"user@example.com\", \"role\": \"USER\", \"status\": true}"
          )
        )
      ),
      @ApiResponse(
        responseCode = "404",
        description = "User not found",
        content = @Content(
          mediaType = "application/json",
          examples = @ExampleObject(
            name = "error",
            value = "{\"code\": \"RESOURCE_NOT_FOUND\", \"message\": \"The requested resource was not found\", \"details\": \"No record with the ID 1 was found in the database.\"}"
          )
        )
      )
    }
  )
  public ResponseEntity<?> findUser(@PathVariable int id) {
    // *** No hay validaciones menores para realizar
    Optional<User> userOptional = userService.findById(id);
    if (userOptional.isEmpty()) {

      Map<String, Object> errorDetails = new HashMap<>();
      errorDetails.put("code", "RESOURCE_NOT_FOUND");
      errorDetails.put("message", "The requested resource was not found");
      errorDetails.put("details", "No record with the ID " + id + " was found in the database.");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);

      // Enviar un código de estado HTTP 404 Not Found con un mensaje en el cuerpo
      //! return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The requested resource was not found");
    }
    // Creamos un DTO para retornar el objeto al frontend
    User user = userOptional.get();
    UserResponseDto response = mapUserToResponseDto(user);

    return ResponseEntity.ok(response);
  }

  @PutMapping
  @Transactional
  @Operation(
    summary = "Update User",
    description = "Update an existing user",
    tags = {"User"},
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "User update data",
      required = true,
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = UserUpdateDto.class)
      )
    ),
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "User updated successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = UserResponseDto.class),
          examples = @ExampleObject(
            name = "example",
            value = "{\"id\": 1, \"email\": \"user@example.com\", \"role\": \"USER\", \"status\": true}"
          )
        )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Invalid input or resource not found",
        content = @Content(
          mediaType = "application/json",
          examples = @ExampleObject(
            name = "error",
            value = "{\"message\": \"Invalid input or resource not found\"}"
          )
        )
      )
    }
  )
  public ResponseEntity<?> updateUser(@RequestBody @Valid UserUpdateDto userUpdateDto) {
    //! NO PERMITE ACTUALIZAR ROL o STATUS
    // extraemos los datos recibidos del DTO
    int id = userUpdateDto.id();
    String password = userUpdateDto.password();
    String email = userUpdateDto.email();
    // Optional<String> email = Optional.ofNullable(userUpdateDto.email());
    String newPassword = userUpdateDto.newPassword();
    String confirmPassword = userUpdateDto.confirmPassword();

    // A continuación verificamos el email y posible cambio de password
    Map<String, Object> errors = new HashMap<>();
    errors = validateEmailRequirements(errors, email);
    errors = validatePasswordChange(errors, password, newPassword, confirmPassword);
    if (!errors.isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("errors", errors));
    }

    try {
      User user = userService.update(id, email, password, newPassword);
      // Creamos un DTO para retornar el objeto al frontend
      UserResponseDto response = mapUserToResponseDto(user);

      return ResponseEntity.ok(response);
    } catch (ResourceNotFoundException e) {
      String errorMessage = "Resource not found with ID: %d".formatted(id);
      return ResponseEntity.badRequest().body(errorMessage);
    } catch (EmailAlreadyExistsException | PasswordsDoNotMatchException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
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
  @Transactional
  @Operation(
    summary = "Delete User",
    description = "Delete a user by their ID",
    tags = {"User"},
    parameters = @io.swagger.v3.oas.annotations.Parameter(
      name = "id",
      description = "ID of the user to be deleted",
      required = true,
      example = "1"
    ),
    responses = {
      @ApiResponse(
        responseCode = "204",
        description = "User deleted successfully"
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Resource not found",
        content = @Content(
          mediaType = "application/json",
          examples = @ExampleObject(
            name = "error",
            value = "{\"message\": \"Resource not found\"}"
          )
        )
      )
    }
  )
  public ResponseEntity<?> deleteUser(@PathVariable int id) {
    try {
      userService.delete(id);
      return ResponseEntity.noContent().build();
    } catch (EntityNotFoundException e) {
      String errorMessage = "Resource not found with ID: %d".formatted(id);
      return ResponseEntity.badRequest().body(errorMessage);
    }
  }

  private UserResponseDto mapUserToResponseDto(User user) {
    return new UserResponseDto(
        user.getId(),
        user.getEmail(),
        user.getRole(),
        user.isStatus());
  }

  private Map<String, Object> validateEmailRequirements(Map<String, Object> errors, String email) {
    if (email == null) {
      return errors; // Early return for null email
    }
    String regex = "^[a-zA-Z0-9_+.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"; // Email validation regex

    if (!Pattern.matches(regex, email)) {
      errors.put("Email", "email is invalid");
    }

    return errors;
  }

  private Map<String, Object> validatePasswordRequirements(Map<String, Object> errors, String password) {
    // ToDo: agregar más validaciones para el password
    // e.i. que contenga una mayúscula, un número, un símbolo, extensión minima de 6 caracteres, etc.
    // se puede usar un regex
    if (password.isEmpty() || password.isBlank()) {
      errors.put("Password", "Password cannot be empty or blank");
    }

    if (password.length() < 5) {
      errors.put("Password", "Password cannot be less than 5 characters");
    }

    return errors;
  }

  private Map<String, Object> validatePasswordChange(Map<String, Object> errors, String password, String newPassword,
      String confirmPassword) {
    /* 
    *  Verificar que... si viene newPassword, entonces también debe tener confirmPassword &
    *  si viene confirmPassword, entonces también debe tener newPassword 
    */
    // Sí newPassword es null, entonces no se quiere cambiar password, aquí acaba todo
    if (newPassword == null) {
      return errors;
    }
    // verificación que el nuevo password cumpla con los requisitos de complejidad
    errors = validatePasswordRequirements(errors, newPassword);

    if (newPassword.equals(password)) {
      errors.put("newPassword", "newPassword cannot be the same as the current password");
    }

    if (confirmPassword == null) {
      errors.put("confirmPassword", "confirmPassword cannot be null if you want to change password");
      return errors; // Early return for null confirmPassword
    }

    if (!newPassword.equals(confirmPassword)) {
      errors.put("newPassword & confirmPassword", "newPassword and confirmPassword do not match");
    }

    return errors;
  }
}
