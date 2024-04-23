package com.unaux.dairo.api.domain.client;

import com.unaux.dairo.api.domain.appointment.Appointment;
import com.unaux.dairo.api.domain.employee.Employee;
import com.unaux.dairo.api.domain.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "client")
public class Client {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "birthday", nullable = false)
  private LocalDate birthday;

  @Column(name = "last_name", nullable = false, length = 50)
  private String lastName;

  @Column(name = "name", nullable = false, length = 50)
  private String name;

  @Column(name = "phone", nullable = false, length = 15)
  private String phone;

  @Column(name = "type", nullable = false, length = 50)
  private String type;

  @OneToOne(mappedBy = "client", cascade = CascadeType.ALL)
  private User user;

  @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
  private Set<Employee> employees;

  @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
  private Set<Appointment> appointments;

  public Client(
      LocalDate birthday,
      String lastName,
      String name,
      String phone) {
    setBirthday(birthday);
    setLastName(lastName);
    setName(name);
    setPhone(phone);
    setType("new client");
  }

  public void update(ClientUpdateDto clientUpdateDto) {
    if (clientUpdateDto.birthday() != null) {
      setBirthday(LocalDate.parse(clientUpdateDto.birthday()));
    }
    if (clientUpdateDto.lastName() != null) {
      setLastName(clientUpdateDto.lastName());
    }
    if (clientUpdateDto.name() != null) {
      setName(clientUpdateDto.name());
    }
    if (clientUpdateDto.phone() != null) {
      setPhone(clientUpdateDto.phone());
    }
    if (clientUpdateDto.type() != null) {
      setType(clientUpdateDto.type());
    }
    if (clientUpdateDto.email() != null) {
      this.user.setEmail(clientUpdateDto.email());
    }
    if (clientUpdateDto.newPassword() != null) {
      this.user.setPassword(encryptPassword(clientUpdateDto.newPassword()));
    }
  }

  public String encryptPassword(String password) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    return encoder.encode(password);
  }

  /*
   * public Client(ClientCreateDto clientCreateDto) {
   * setStatus(true);
   * setName(clientCreateDto.name());
   * setLastName(clientCreateDto.lastName());
   * setPhone(clientCreateDto.phone());
   * setBirthday(LocalDate.parse(clientCreateDto.birthday()));
   * setEmail(clientCreateDto.email());
   * setPassword(encryptPassword(clientCreateDto.password()));
   * setType("new"); // por defecto todos serán nuevos
   * setStatus(true);
   * 
   * }
   * 
   * 
   * 
   * public void inactivate() {
   * setStatus(false);
   * }
   */

}
