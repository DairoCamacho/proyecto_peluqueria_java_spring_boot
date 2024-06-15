package com.unaux.dairo.api.domain.appointment;

import java.time.LocalDate;
import java.time.LocalTime;

import org.hibernate.Hibernate;

// import org.hibernate.annotations.GenericGenerator;

import com.unaux.dairo.api.domain.client.Client;
import com.unaux.dairo.api.domain.employee.Employee;
import com.unaux.dairo.api.domain.product.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "appointment")
public class Appointment {

  @Id
  // @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "appointmentIdGenerator")
  // @GenericGenerator(name = "appointmentIdGenerator",strategy = "com.unaux.dairo.api.domain.appointment.AppointmentIdGenerator")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "date", nullable = false)
  private LocalDate date;

  @Column(name = "time", nullable = false)
  private LocalTime time;

  @Column(name = "condition", nullable = false, length = 45)
  private String condition;

  @Column(name = "notes", length = 45)
  private String notes;

  @Column(name = "status", nullable = false)
  private boolean status;

  @ManyToOne
  @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
  private Product product;

  @ManyToOne
  @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
  private Employee employee;

  @ManyToOne
  @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
  private Client client;

  public Appointment(LocalDate date2, LocalTime time2, Product product2, Employee employee2, Client client2,
      String notes2) {
    setDate(date2);
    setTime(time2);
    setProduct(product2);
    setEmployee(employee2);
    setClient(client2);
    setNotes(notes2);
    setStatus(true);
    setCondition("pending");
  }

  public void update(LocalDate date3, LocalTime time3, Product product3, Employee employee3, String notes3) {
    if (date3 != null) {
      setDate(date3);
    }
    if (time3 != null) {
      setTime(time3);
    }
    if (product3.getId() > 0) {
      setProduct(product3);
    }
    if (employee3.getId() > 0) {
      setEmployee(employee3);
    }
    if (notes3 != null) {
      setNotes(notes3);
    }
  }

  public void inactivate() {
    setStatus(false);
  }

  @Override
  public String toString() {
    if (Hibernate.isInitialized(this)) {
      return "Appointment [id=" + id + ", dateAppointment=" + date + ", timeAppointment=" + time
          + ", conditionAppointment=" + condition + ", notes=" + notes + ", status=" + status + "]";
    } else {
      return "Appointment (Proxy)";
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
