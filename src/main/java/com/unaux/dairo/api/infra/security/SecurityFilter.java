package com.unaux.dairo.api.infra.security;

import com.unaux.dairo.api.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
// OncePerRequestFilter: Garantiza que el filtro se ejecute una vez por cada petición.
public class SecurityFilter extends OncePerRequestFilter {

  private final TokenService tokenService;

  private final UserRepository userRepository;

  SecurityFilter(TokenService tokenService, UserRepository userRepository) {
    this.tokenService = tokenService;
    this.userRepository = userRepository;
  }

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    // Obtiene el token de autorización (Bearer token) desde la cabecera "Authorization" de la solicitud.
    var authHeader = request.getHeader("Authorization");

    if (authHeader != null) {
      // Remueve la parte "Bearer " del token, dejando solo la parte del token en sí.
      var token = authHeader.replace("Bearer ", "");

      // extraemos el email (usuario) que viene dentro del token
      // y lo almacena en la variable subject
      var subject = tokenService.getSubject(token);

      // si el email no es nulo
      if (subject != null) {
        // se puede agregar otra validación así:
        // && SecurityContextHolder.getContext().getAuthentication() == null sirve
        // para verificar que no esta aun autenticado

        // encontramos al usuario en la base de datos
        var user = userRepository.findByEmail(subject);

        // forzamos el inicio de sesión
        var authentication = new UsernamePasswordAuthenticationToken(
          user,
          null,
          user.getAuthorities()
        );
        // settiamos manualmente esa autenticación
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }
    // Invoca a filterChain.doFilter para continuar con el procesamiento de la solicitud hacia los siguientes filtros o el recurso final.
    filterChain.doFilter(request, response);
  }
}
