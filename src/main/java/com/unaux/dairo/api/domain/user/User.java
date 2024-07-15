package com.unaux.dairo.api.domain.user;

import java.util.Collection;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.unaux.dairo.api.domain.client.Client;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "[user]")
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "email", nullable = false, unique = true, length = 255)
  private String email;

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "role", nullable = false, length = 255)
  private Role role;

  @Column(name = "password", nullable = false, length = 300)
  private String password;

  @Transient // significa que este atributo no se mapea, para no crearlo en la DB
  private String confirmPassword;

  @Column(name = "status", nullable = false)
  private boolean status;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private Client client;

  public User(String email2, String password2) {
    setEmail(email2);
    setPassword(encryptPassword(password2));
    setRole(Role.CLIENT); // por defecto todos se crean como Client
    setStatus(true);
  }

  public void update(String email3, String newPassword3) {
    if (email3 != null) {
      setEmail(email3);
    }
    if (newPassword3 != null) {
      setPassword(encryptPassword(newPassword3));
    }
  }

  public String encryptPassword(String password) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    return encoder.encode(password);
  }

  public void inactivate() {
    setStatus(false);
  }

  // Este método devuelve una colección de autoridades (roles) asignadas al usuario.
  // En este caso, se crea una lista con una sola autoridad: "ROLE_USER".
  // La autoridad "ROLE_USER" es comúnmente utilizada para representar a los usuarios normales en un sistema.
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
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

  @Override
  public String toString() {
    if (Hibernate.isInitialized(this)) {
      return "User [id=" + id + ", email=" + email + ", role=" + role + ", status=" + status
          + ", client=" + client.getId() + "]";
    } else {
      return "Appointment (Proxy)";
    }
  }
}
