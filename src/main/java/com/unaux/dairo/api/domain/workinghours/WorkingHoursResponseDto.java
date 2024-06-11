package com.unaux.dairo.api.domain.workinghours;

import java.time.LocalDateTime;

public record WorkingHoursResponseDto(
  int id,
  LocalDateTime startDate,
  LocalDateTime endDate,
  int employeeId,
  boolean status
) {}
