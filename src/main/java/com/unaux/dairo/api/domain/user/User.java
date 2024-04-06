package com.unaux.dairo.api.domain.user;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String name;

  @Column(name = "last_name")
  private String lastName;

  private LocalDate birthday;
  private String phone;
  private String email;
  private String password;
  private boolean status;

  @Transient // no se mapea para no crearlo en la DB
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
