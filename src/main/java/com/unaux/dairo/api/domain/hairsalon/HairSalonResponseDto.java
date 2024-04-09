package com.unaux.dairo.api.domain.hairsalon;

public record HairSalonResponseDto(
  int id,
  String phone,
  String address,
  String neighborhood,
  String city,
  String country
) {}
