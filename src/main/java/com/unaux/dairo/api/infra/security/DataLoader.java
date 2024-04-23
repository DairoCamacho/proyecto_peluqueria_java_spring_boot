package com.unaux.dairo.api.infra.security;

import com.unaux.dairo.api.domain.client.Client;
import com.unaux.dairo.api.domain.user.User;
import com.unaux.dairo.api.repository.ClientRepository;
import com.unaux.dairo.api.repository.UserRepository;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

  private final ClientRepository clientRepository;

  private final UserRepository userRepository;

  // inyectamos los repositorios
  public DataLoader(
    ClientRepository clientRepository,
    UserRepository userRepository
  ) {
    this.clientRepository = clientRepository;
    this.userRepository = userRepository;
  }

  @Override
  @Transactional
  public void run(String... args) throws Exception {
    // Verificar si el usuario existe
    String email = "user@admin.com";
    boolean existsEmail = userRepository.existsByEmail(email);

    if (!existsEmail) {
      // Crear y guardar el cliente
      Client client = new Client();
      client.setBirthday(LocalDate.of(1990, 5, 15));
      client.setLastName("User");
      client.setName("Admin");
      client.setPhone("3201110022");
      client.setType("VIP");
      Client savedClient = clientRepository.save(client);

      // Crear y guardar el usuario
      User user = new User();
      user.setClient(savedClient);
      user.setEmail("user@admin.com");
      user.setPassword(encryptPassword("admin"));
      user.setRole("1234567890");
      user.setStatus(true);
      userRepository.save(user);
    }
  }

  public String encryptPassword(String password) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    return encoder.encode(password);
  }
}
