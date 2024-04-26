package com.unaux.dairo.api.domain.workinghours;

import com.unaux.dairo.api.domain.employee.Employee;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "working_hours")
public class WorkingHours {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "workingHoursIdGenerator") // Cambiado a SEQUENCE
  @GenericGenerator(name = "workingHoursIdGenerator",strategy = "com.unaux.dairo.api.domain.workinghours.WorkingHoursIdGenerator")
  private String id;

  @Column(name = "start_date", nullable = false)
  private LocalDateTime startDate;

  @Column(name = "end_date")
  private LocalDateTime endDate;

  @ManyToOne
  @JoinColumn(
    name = "employee_id",
    referencedColumnName = "id",
    nullable = false
  )
  private Employee employee;

  public WorkingHours(
    WorkingHoursCreateDto workingHoursCreateDto,
    Employee employee
  ) {
    this.startDate = workingHoursCreateDto.startDate();
    this.endDate = workingHoursCreateDto.endDate();
    this.employee = employee;
  }

  public void update(WorkingHoursUpdateDto workingHoursUpdateDto) {
    if (workingHoursUpdateDto.startDate() != null) {
      setStartDate(workingHoursUpdateDto.startDate());
    }
    if (workingHoursUpdateDto.endDate() != null) {
      setEndDate(workingHoursUpdateDto.endDate());
    }
  }
}
