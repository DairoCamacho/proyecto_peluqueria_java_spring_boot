package com.unaux.dairo.api.domain.user;

import com.unaux.dairo.api.domain.client.Client;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "user")
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @OneToOne
  @JoinColumn(
    name = "client_id",
    referencedColumnName = "id",
    nullable = false,
    unique = true
  )
  private Client client;

  @Column(name = "email", nullable = false, unique = true, length = 255)
  private String email;

  @Column(name = "password", nullable = false, length = 300)
  private String password;

  @Column(name = "role", nullable = false, length = 255)
  private String role;

  @Column(name = "status", nullable = false)
  private boolean status;

  // constructor que usará el cliente para registrarse
  public User(Client client, String email, String password) {
    setClient(client);
    setEmail(email);
    setPassword(encryptPassword(password));
    setRole("client");
    setStatus(true);
  }

  // // constructor que usará el administrador
  // public User(String email, String password, String role) {
  //   setEmail(email);
  //   setPassword(encryptPassword(password));
  //   setRole(role);
  //   setEnable(true);
  // }

  public void update(UserUpdateDto userUpdateDto){
    if (userUpdateDto.email() != null){
      setEmail(userUpdateDto.email());
    }
    if (Objects.equals(userUpdateDto.status(), Boolean.TRUE)) {
      setStatus(userUpdateDto.status());
    }
    if(userUpdateDto.password() != null   ) {
      setPassword(userUpdateDto.password());
    }
    if(userUpdateDto.role() != null){
      setRole(userUpdateDto.role());
    }
  }

  public String encryptPassword(String password) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    return encoder.encode(password);
  }

  public void inactivate() {
    setStatus(false);
  }

  @Transient // significa que no se mapea, para no crearlo en la DB
  private String confirmPassword;

  // Este método devuelve una colección de autoridades (roles) asignadas al usuario.
  // En este caso, se crea una lista con una sola autoridad: "ROLE_USER".
  // La autoridad "ROLE_USER" es comúnmente utilizada para representar a los usuarios normales en un sistema.
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_USER"));
  }

  @Override
  public String getPassword() {
    return password; //atento para cambiar por el atributo de tu clase
  }

  @Override
  public String getUsername() {
    return email; //atento para cambiar por el atributo de tu clase
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
