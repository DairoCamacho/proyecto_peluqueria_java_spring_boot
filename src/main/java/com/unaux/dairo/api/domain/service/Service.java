package com.unaux.dairo.api.domain.service;

import com.unaux.dairo.api.domain.appointment.Appointment;
import com.unaux.dairo.api.domain.hairsalon.HairSalon;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import java.time.LocalTime;
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
@Table(name = "service")
public class Service {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "name", nullable = false, length = 45)
  private String name;

  @Column(name = "price", nullable = false)
  private int price;

  @Column(name = "duration", nullable = false)
  private LocalTime duration;

  @ManyToOne
  @JoinColumn(
    name = "hair_salon_id",
    referencedColumnName = "id",
    nullable = false
  )
  private HairSalon hairSalon;

  @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
  private Set<Appointment> appointments;

  public Service(
    @Valid ServiceCreateDto serviceCreateDto,
    HairSalon hairSalon
  ) {
    this.name = serviceCreateDto.name();
    this.price = serviceCreateDto.price();
    this.duration = serviceCreateDto.duration();
    this.hairSalon = hairSalon;
  }

  public void update(ServiceUpdateDto serviceUpdateDto, HairSalon hairSalon) {
    if (serviceUpdateDto.name() != null) {
      setName(serviceUpdateDto.name());
    }
    if (serviceUpdateDto.price() > 0) {
      setPrice(serviceUpdateDto.price());
    }
    if (serviceUpdateDto.duration() != null) {
      setDuration(serviceUpdateDto.duration());
    }
    if (serviceUpdateDto.hairSalonId() > 0) {
      setHairSalon(hairSalon);
    }
  }
}
