package com.unaux.dairo.api.domain.hairsalon;

import com.unaux.dairo.api.domain.employee.Employee;
import com.unaux.dairo.api.domain.service.Service;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "hair_salon")
public class HairSalon {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "name", nullable = false, length = 45)
  private String name;

  @Column(name = "phone", nullable = false, length = 45)
  private String phone;

  @Column(name = "address", nullable = false, length = 45)
  private String address;

  @Column(name = "neighborhood", nullable = false, length = 45)
  private String neighborhood;

  @Column(name = "city", nullable = false, length = 45)
  private String city;

  @Column(name = "country", nullable = false, length = 45)
  private String country;

  @Column(name = "status", nullable = false)
  private boolean status;

  @OneToMany(mappedBy = "hairSalon", cascade = CascadeType.ALL)
  private Set<Employee> employees;

  @OneToMany(mappedBy = "hairSalon", cascade = CascadeType.ALL)
  private Set<Service> services;

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

  public void inactive() {
    setStatus(false);
  }
}
