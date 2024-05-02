package com.unaux.dairo.api.infra.security;

import java.util.Arrays;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

// Esta clase es una configuración de seguridad que utiliza anotaciones de Spring para definir cómo se debe manejar la seguridad en la aplicación.
// Está anotada con @Configuration, lo que indica que contiene configuración de beans.
// También está anotada con @EnableWebSecurity, lo que habilita la seguridad web en la aplicación.
@Configuration
@EnableWebSecurity
// @EnableMethodSecurity // remplaza el uso de @EnableGlobalMethodSecurity(prePostEnabled = true) 
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
    httpSecurity
      .cors(cors -> {
        CorsConfigurationSource source = request -> {
          CorsConfiguration config = new CorsConfiguration();
          config.setAllowedOriginPatterns(Arrays.asList("*"));
          // config.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // Reemplaza con los orígenes permitidos
          config.setAllowedMethods(
            Arrays.asList("GET", "POST", "PUT", "DELETE")
          );
          config.setAllowedHeaders(Arrays.asList("*"));
          config.setAllowCredentials(true);
          return config;
        };
        cors.configurationSource(source);
      })
      .csrf(csrf -> csrf.disable())
      .sessionManagement(management ->
        management.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      )
      .authorizeHttpRequests(authorize ->
        authorize
          .requestMatchers(HttpMethod.POST,"/api/login", "/api/client").permitAll()
          .anyRequest().authenticated() // Cualquier otra solicitud que no coincida con las reglas anteriores requerirá autenticación. Permite acceso a cualquier otro recurso Sí está autenticado
          // .anyRequest().denyAll() // Se deniega el acceso a cualquier URL que aún no haya coincidido. Esta es una buena estrategia si no quiere olvidarse accidentalmente de actualizar sus reglas de autorización.
          // .requestMatchers("/admin/**").hasRole("ADMIN")
          // .requestMatchers("/**").hasRole("USER")
          // .requestMatchers("/**").hasAnyRole("USER", "ADMIN")
      )
      .addFilterBefore(
        securityFilter,
        UsernamePasswordAuthenticationFilter.class
      );
    return httpSecurity.build();
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
