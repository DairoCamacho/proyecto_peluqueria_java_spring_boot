package com.unaux.dairo.api.domain.client;

import java.time.LocalDate;

public record ClientFindDto(
  int id,
  LocalDate birthday,
  String lastName,
  String name,
  String phone,
  String type,
  String email,
  boolean status
) {
  public ClientFindDto(Client client) {
    this(
      client.getId(),
      client.getBirthday(),
      client.getLastName(),
      client.getName(),
      client.getPhone(),
      client.getType(),
      client.getUser().getEmail(),
      client.getUser().isStatus()
    );
  }
}
