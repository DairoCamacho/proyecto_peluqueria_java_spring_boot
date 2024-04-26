package com.unaux.dairo.api.domain.workinghours;

import java.io.Serializable;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class WorkingHoursIdGenerator implements IdentifierGenerator {

  @Override
    public Serializable generate(SharedSessionContractImplementor session, Object entity) {
        WorkingHours workingHours = (WorkingHours) entity;

        String employeeId = String.valueOf(workingHours.getEmployee().getId());
        String startDateString = workingHours.getStartDate().toString();
        String endDateString = workingHours.getEndDate().toString();

        String id = employeeId + "--" + startDateString + "--" + endDateString;

        return id;
  }
}
