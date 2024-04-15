package com.unaux.dairo.api.domain.hairsalon;

public record HairSalonResponseDto(
  int id,
  String name,
  String phone,
  String address,
  String neighborhood,
  String city,
  String country
) {}
