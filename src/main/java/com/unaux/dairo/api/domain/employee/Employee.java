package com.unaux.dairo.api.domain.employee;

import com.unaux.dairo.api.domain.appointment.Appointment;
import com.unaux.dairo.api.domain.client.Client;
import com.unaux.dairo.api.domain.hairsalon.HairSalon;
import com.unaux.dairo.api.domain.workinghours.WorkingHours;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table
public class Employee {

  @Id
  @OneToOne
  @JoinColumn(name = "id", referencedColumnName = "id")
  private Client client;

  @Column(name = "position", nullable = false, length = 45)
  private String position;

  @Column(name = "hire_date", nullable = false)
  private LocalDate hireDate;

  @Column(name = "termination_date")
  private LocalDate terminationDate;

  @ManyToOne
  @JoinColumn(
    name = "hair_salon_id",
    referencedColumnName = "id",
    nullable = false
  )
  private HairSalon hairSalon;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
  private Set<WorkingHours> workingHours;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
  private Set<Appointment> appointments;
}
