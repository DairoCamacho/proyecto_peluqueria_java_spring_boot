package com.unaux.dairo.api.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Esta clase es una configuración de seguridad que utiliza anotaciones de Spring para definir cómo se debe manejar la seguridad en la aplicación.
// Está anotada con @Configuration, lo que indica que contiene configuración de beans.
// También está anotada con @EnableWebSecurity, lo que habilita la seguridad web en la aplicación.
@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

  private final SecurityFilter securityFilter;

  SecurityConfigurations(SecurityFilter securityFilter) {
    this.securityFilter = securityFilter;
  }

  // Este método crea y configura un filtro de seguridad llamado springSecurityFilterChain.
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)
    throws Exception {
    // El filtro se encarga de manejar la seguridad en la aplicación, como proteger las URL, validar las credenciales de inicio de sesión y redirigir al formulario de inicio de sesión.
    // En este caso, se deshabilita la protección CSRF (Cross-Site Request Forgery) y se establece la política de creación de sesiones como “sin estado” (STATELESS).
    return httpSecurity
      .csrf()
      .disable()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      .authorizeRequests()
      .requestMatchers(HttpMethod.POST, "api/login")
      .permitAll()
      .anyRequest()
      .authenticated()
      .and()
      .addFilterBefore(
        securityFilter,
        UsernamePasswordAuthenticationFilter.class
      )
      .build();
  }

  // Este método crea y configura el AuthenticationManager, el cual es responsable de autenticar a los usuarios.
  // Se obtiene a través de la inyección de dependencias de AuthenticationConfiguration.
  @Bean
  public AuthenticationManager authenticationManager(
    AuthenticationConfiguration authenticationConfiguration
  ) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
