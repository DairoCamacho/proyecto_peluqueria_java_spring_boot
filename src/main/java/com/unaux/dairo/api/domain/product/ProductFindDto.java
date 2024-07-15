package com.unaux.dairo.api.domain.product;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record ProductFindDto(
  @NotNull int id,
  String name,
  int price,
  LocalTime duration,
  int hairSalonId,
  Boolean status
) {
  public ProductFindDto(Product product) {
    this(
      product.getId(),
      product.getName(),
      product.getPrice(),
      product.getDuration(),
      product.getHairSalon().getId(),
      product.isStatus()
    );
  }
}
