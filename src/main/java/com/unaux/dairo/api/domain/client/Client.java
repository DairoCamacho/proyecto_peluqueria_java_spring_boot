package com.unaux.dairo.api.domain.client;

import java.time.LocalDate;
import java.util.Set;

import org.hibernate.Hibernate;

import com.unaux.dairo.api.domain.appointment.Appointment;
import com.unaux.dairo.api.domain.employee.Employee;
import com.unaux.dairo.api.domain.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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

  @Column(name = "status", nullable = false)
  private boolean status;

  @OneToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
  private User user;

  @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
  private Set<Employee> employees;

  @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
  private Set<Appointment> appointments;

  public Client(LocalDate birthday2, String lastName2, String name2, String phone2, User user2) {
    setBirthday(birthday2);
    setLastName(lastName2);
    setName(name2);
    setPhone(phone2);
    setType("new client");
    setStatus(true);
    setUser(user2);
  }

  public void update(LocalDate birthday3, String lastName3, String name3, String phone3) {

    if (birthday3 != null) {
      setBirthday(birthday3);
    }
    if (lastName3 != null) {
      setLastName(lastName3);
    }
    if (name3 != null) {
      setName(name3);
    }
    if (phone3 != null) {
      setPhone(phone3);
    }
  }

  public void inactivate() {
    setStatus(false);
  }

  @Override
  public String toString() {
    if (Hibernate.isInitialized(this)) {
      return "Client [id=" + id + ", birthday=" + birthday + ", lastName=" + lastName + ", name=" + name + ", phone="
          + phone + ", type=" + type + ", status=" + status + "]";
    } else {
      return "Client (Proxy)";
    }
  }

  /*
   Hibernate utiliza proxies para cargar entidades de forma perezosa. Esto significa que recupera datos de la base de datos solo cuando es necesario, lo que mejora el rendimiento.
  Cuando llamas a toString() en un objeto proxy, Hibernate intenta acceder a sus datos subyacentes, que podrían no estar cargados todavía.
  Esto desencadena una cadena de llamadas donde cada objeto intenta convertirse a una cadena, lo que resulta en un bucle infinito.
  
  Personalizar toString() para proxies:
  Si solo necesitas información específica del cliente para el registro, considera anular el método toString() en tu clase Client. 
  Este método puede verificar si el objeto es un proxy y devolver la información deseada directamente, evitando el bucle infinito.
   */
}
