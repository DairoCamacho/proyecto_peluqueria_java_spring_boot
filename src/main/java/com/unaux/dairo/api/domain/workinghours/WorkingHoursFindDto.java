package com.unaux.dairo.api.domain.workinghours;

import java.time.LocalDateTime;

public record WorkingHoursFindDto(
  int id,
  LocalDateTime startDate,
  LocalDateTime endDate,
  int employeeId
) {
  public WorkingHoursFindDto(WorkingHours workingHours) {
    this(
      workingHours.getId(),
      workingHours.getStartDate(),
      workingHours.getEndDate(),
      workingHours.getEmployee().getId()
    );
  }
}
