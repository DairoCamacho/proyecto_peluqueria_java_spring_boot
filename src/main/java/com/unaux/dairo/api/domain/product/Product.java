package com.unaux.dairo.api.domain.product;

import java.time.LocalTime;
import java.util.Set;

import org.hibernate.Hibernate;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "product")
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "name", nullable = false, length = 45)
  private String name;

  @Column(name = "price", nullable = false)
  private int price;

  @Column(name = "duration", nullable = false)
  private LocalTime duration;

  @Column(name = "status", nullable = false)
  private boolean status;

  @ManyToOne
  @JoinColumn(name = "hair_salon_id", referencedColumnName = "id", nullable = false)
  private HairSalon hairSalon;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
  private Set<Appointment> appointments;

  public Product(String name2, int price2, LocalTime duration2, HairSalon hairSalon2) {
    setName(name2);
    setPrice(price2);
    setDuration(duration2);
    setHairSalon(hairSalon2);
    setStatus(true);
  }

  public void update(String name3, int price3, LocalTime duration3, HairSalon hairSalon3, Boolean status3) {

    if (name3 != null) {
      setName(name3);
    }
    if (price3 > 0) {
      setPrice(price3);
    }
    if (duration3 != null) {
      setDuration(duration3);
    }
    if (hairSalon3 != null) {
      setHairSalon(hairSalon3);
    }
    if (status3 != null) {
      setStatus(status3);
    }
  }

  public void inactivate() {
    setStatus(false);
  }

  @Override
  public String toString() {
    if (Hibernate.isInitialized(this)) {
      return "Product [id=" + id + ", name=" + name + ", price=" + price + ", duration=" + duration
          + ", status=" + status + "]";
    } else {
      return "Appointment (Proxy)";
    }
  }
}
