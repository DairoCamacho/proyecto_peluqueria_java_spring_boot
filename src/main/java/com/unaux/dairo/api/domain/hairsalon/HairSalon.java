package com.unaux.dairo.api.domain.hairsalon;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "hair_salon")
public class HairSalon {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
  
    private String name;
    private String phone;
    private String address;
    private String neighborhood;
    private String city;
    private String country;

  public HairSalon(@Valid HairSalonCreateDto hairSalonCreateDto) {
    setName(hairSalonCreateDto.name());
    setPhone(hairSalonCreateDto.phone());
    setAddress(hairSalonCreateDto.address());
    setNeighborhood(hairSalonCreateDto.neighborhood());
    setCity(hairSalonCreateDto.city());
    setCountry(hairSalonCreateDto.country());
  }

  public void update(HairSalonUpdateDto hairSalonUpdateDto) {
    if (hairSalonUpdateDto.name() != null) {
      setName(hairSalonUpdateDto.name());
    }
    if (hairSalonUpdateDto.phone() != null) {
      setPhone(hairSalonUpdateDto.phone());
    }
    if (hairSalonUpdateDto.address() != null) {
      setAddress(hairSalonUpdateDto.address());
    }
    if (hairSalonUpdateDto.neighborhood() != null) {
      setNeighborhood(hairSalonUpdateDto.neighborhood());
    }
    if (hairSalonUpdateDto.city() != null) {
      setCity(hairSalonUpdateDto.city());
    }
    if (hairSalonUpdateDto.country() != null) {
      setCountry(hairSalonUpdateDto.country());
    }
  }
}
