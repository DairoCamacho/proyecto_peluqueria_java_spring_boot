package com.unaux.dairo.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unaux.dairo.api.domain.user.User;
import com.unaux.dairo.api.domain.user.UserAuthenticationDto;
import com.unaux.dairo.api.infra.security.JwtTokenDto;
import com.unaux.dairo.api.infra.security.TokenService;

import jakarta.validation.Valid;

// Esta clase es un controlador REST que maneja las solicitudes relacionadas con la autenticación de usuarios.
@RestController
@RequestMapping("api/v1/login")
public class AuthenticationController {

  // para disparar el proceso de autenticación en spring existe la clase
  // AuthenticationManager
  private final AuthenticationManager authenticationManager; // es una interfaz proporcionada por Spring Security para manejar la autenticación.

  private final TokenService tokenService;

  AuthenticationController(
      AuthenticationManager authenticationManager,
      TokenService tokenService) {
    this.authenticationManager = authenticationManager;
    this.tokenService = tokenService;
  }

  // Este método toma un objeto UserAuthenticationDto como parámetro, que
  // probablemente contiene las credenciales del usuario (email y password)
  @PostMapping
  public ResponseEntity<JwtTokenDto> authenticateUser(
      @RequestBody @Valid UserAuthenticationDto userAuthenticationDto) {
    // creamos un token con las credenciales del usuario
    Authentication authToken = new UsernamePasswordAuthenticationToken(
        userAuthenticationDto.email(),
        userAuthenticationDto.password());

    // se llama al método authenticate del AuthenticationManager para verificar las
    // credenciales del usuario.
    var authenticatedUser = authenticationManager.authenticate(authToken);

    var JwtToken = tokenService.createToken(
        (User) authenticatedUser.getPrincipal());

    // Si la autenticación es exitosa, devuelve una respuesta HTTP 200 (OK)
    return ResponseEntity.ok(new JwtTokenDto(JwtToken));
  }
}
