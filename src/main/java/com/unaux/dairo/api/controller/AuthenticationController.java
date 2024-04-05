package com.unaux.dairo.api.controller;

import com.unaux.dairo.api.domain.user.UserAuthenticationDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Esta clase es un controlador REST que maneja las solicitudes relacionadas con la autenticación de usuarios.
@RestController
@RequestMapping("api/login")
public class AuthenticationController {

  // para disparar el proceso de autenticación en spring existe la clase AuthenticationManager
  @Autowired
  private AuthenticationManager authenticationManager; // es una interfaz proporcionada por Spring Security para manejar la autenticación.

  // Este método toma un objeto UserAuthenticationDto como parámetro, que probablemente contiene las credenciales del usuario (email y password)
  @PostMapping
  public ResponseEntity authenticateUser(
    @RequestBody @Valid UserAuthenticationDto userAuthenticationDto
  ) {
    // creamos un token con las credenciales del usuario
    Authentication authToken = new UsernamePasswordAuthenticationToken(
      userAuthenticationDto.email(),
      userAuthenticationDto.password()
    );

    // se llama al método authenticate del AuthenticationManager para verificar las credenciales del usuario.
    authenticationManager.authenticate(authToken);

    // Si la autenticación es exitosa, devuelve una respuesta HTTP 200 (OK)
    return ResponseEntity.ok().build();
  }
}
