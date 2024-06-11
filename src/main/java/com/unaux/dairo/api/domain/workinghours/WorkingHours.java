package com.unaux.dairo.api.domain.workinghours;

import java.time.LocalDateTime;

import org.hibernate.Hibernate;

// import org.hibernate.annotations.GenericGenerator;

import com.unaux.dairo.api.domain.employee.Employee;

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
@Table(name = "working_hours")
public class WorkingHours {

  @Id
  // @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "workingHoursIdGenerator")
  // @GenericGenerator(name = "workingHoursIdGenerator",strategy = "com.unaux.dairo.api.domain.workinghours.WorkingHoursIdGenerator")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "start_date", nullable = false)
  private LocalDateTime startDate;

  @Column(name = "end_date")
  private LocalDateTime endDate;

  @Column(name = "status", nullable = false)
  private boolean status;

  @ManyToOne
  @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
  private Employee employee;

  public WorkingHours(LocalDateTime startDate2, LocalDateTime endDate2, Employee employee2) {
    setStartDate(startDate2);
    setEndDate(endDate2);
    setEmployee(employee2);
    setStatus(true);
  }

  public void update(LocalDateTime startDate3, LocalDateTime endDate3) {
    if (startDate3 != null) {
      setStartDate(startDate3);
    }
    if (endDate3 != null) {
      setEndDate(endDate3);
    }
  }

  public void inactivate() {
    setStatus(false);
  }

  @Override
  public String toString() {
    if (Hibernate.isInitialized(this)) {
      return "WorkingHours [id=" + id + ", startDate=" + startDate + ", endDate=" + endDate + ", status="
          + status + "]";
    } else {
      return "Appointment (Proxy)";
    }
  }

}
