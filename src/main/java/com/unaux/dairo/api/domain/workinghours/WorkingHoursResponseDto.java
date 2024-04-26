package com.unaux.dairo.api.domain.workinghours;

import java.time.LocalDateTime;

public record WorkingHoursResponseDto(
  String id,
  LocalDateTime startDate,
  LocalDateTime endDate,
  int employeeId
) {}
