package com.unaux.dairo.api.domain.employee;

import java.time.LocalDate;
import java.util.Set;

import org.hibernate.Hibernate;

import com.unaux.dairo.api.domain.appointment.Appointment;
import com.unaux.dairo.api.domain.client.Client;
import com.unaux.dairo.api.domain.hairsalon.HairSalon;
import com.unaux.dairo.api.domain.workinghours.WorkingHours;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@EqualsAndHashCode
@Entity
@Table
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  @JoinColumn(name = "client_id", referencedColumnName = "id")
  private Client client;

  @Column(name = "position", nullable = false, length = 45)
  private String position;

  @Column(name = "hire_date", nullable = false)
  private LocalDate hireDate;

  @Column(name = "termination_date", nullable = true)
  private LocalDate terminationDate;

  @Column(name = "status", nullable = false)
  private boolean status;

  @ManyToOne
  @JoinColumn(name = "hair_salon_id", referencedColumnName = "id")
  private HairSalon hairSalon;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
  private Set<WorkingHours> workingHours;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
  private Set<Appointment> appointments;

  public Employee(Client client2, String position2, LocalDate hireDate2, HairSalon hairSalon2) {
    setClient(client2);
    setPosition(position2);
    setHireDate(hireDate2);
    setHairSalon(hairSalon2);
    setStatus(true);
  }

  public void update(String position3, LocalDate hireDate3, LocalDate terminationDate3, Boolean status3,
      HairSalon hairSalon3) {
    if (position3 != null) {
      setPosition(position3);
    }
    if (hireDate3 != null) {
      setHireDate(hireDate3);
    }
    if (terminationDate3 != null) {
      setTerminationDate(terminationDate3);
    }
    if (status3 != null) {
      setStatus(status3);
    }
    // por defecto siempre viene un HairSalon desde el Servicio, sea el mismo que ya tiene o uno nuevo para actualizar
    setHairSalon(hairSalon3);
  }

  public void inactivate() {
    setStatus(false);
  }

  @Override
  public String toString() {
    if (Hibernate.isInitialized(this)) {
      return "Employee [id=" + id + ", client=" + client + ", position=" + position + ", hireDate=" + hireDate
          + ", terminationDate=" + terminationDate + ", status=" + status + "]";
    } else {
      return "Appointment (Proxy)";
    }
  }
}
