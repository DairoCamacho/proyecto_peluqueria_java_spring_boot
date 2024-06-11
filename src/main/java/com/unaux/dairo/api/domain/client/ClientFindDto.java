package com.unaux.dairo.api.domain.client;

import java.time.LocalDate;

public record ClientFindDto(
  int id,
  LocalDate birthday,
  String lastName,
  String name,
  String phone,
  boolean status,
  String type,
  int userId
) {
  public ClientFindDto(Client client) {
    this(
      client.getId(),
      client.getBirthday(),
      client.getLastName(),
      client.getName(),
      client.getPhone(),
      client.isStatus(),
      client.getType(),
      client.getUser().getId()
    );
  }
}
