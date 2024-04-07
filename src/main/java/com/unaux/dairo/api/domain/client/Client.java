package com.unaux.dairo.api.domain.client;

import com.unaux.dairo.api.domain.user.User;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "client")
@PrimaryKeyJoinColumn
public class Client extends User {

  private String type;

  public Client(ClientCreateDto clientCreateDto) {
    setStatus(true);
    setName(clientCreateDto.name());
    setLastName(clientCreateDto.lastName());
    setPhone(clientCreateDto.phone());
    setBirthday(LocalDate.parse(clientCreateDto.birthday()));
    setEmail(clientCreateDto.email());
    setPassword(encryptPassword(clientCreateDto.password()));
    setType("new"); // por defecto todos serán nuevos
  }

  public void update(ClientUpdateDto clientUpdateDto) {
    if (clientUpdateDto.name() != null) {
      setName(clientUpdateDto.name());
    }
    if (clientUpdateDto.lastName() != null) {
      setLastName(clientUpdateDto.lastName());
    }
    if (clientUpdateDto.phone() != null) {
      setPhone(clientUpdateDto.phone());
    }
    if (clientUpdateDto.birthday() != null) {
      setBirthday(LocalDate.parse(clientUpdateDto.birthday()));
    }
    if (clientUpdateDto.type() != null) {
      setType(clientUpdateDto.type());
    }
    if (clientUpdateDto.email() != null) {
      setEmail(clientUpdateDto.email());
    }
    if (clientUpdateDto.newPassword() != null) {
      setPassword(encryptPassword(clientUpdateDto.newPassword()));
    }
  }

  public void inactivate() {
    setStatus(false);
  }

  public String encryptPassword(String password) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    return encoder.encode(password);
  }

  public boolean matchesPassword(String rawPassword, String encodedPassword) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    return encoder.matches(rawPassword, encodedPassword);
  }
}
