package com.unaux.dairo.api.domain.hairsalon;

import java.util.Set;

import org.hibernate.Hibernate;

import com.unaux.dairo.api.domain.employee.Employee;
import com.unaux.dairo.api.domain.product.Product;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
  private Set<Product> products;

  public HairSalon(String name2, String phone2, String address2, String neighborhood2, String city2, String country2) {
    setName(name2);
    setPhone(phone2);
    setAddress(address2);
    setNeighborhood(neighborhood2);
    setCity(city2);
    setCountry(country2);
    setStatus(true);
  }

  public void update(String name3, String phone3, String address3, String neighborhood3, String city3, String country3,
      Boolean status3) {
    if (name3 != null) {
      setName(name3);
    }
    if (phone3 != null) {
      setPhone(phone3);
    }
    if (address3 != null) {
      setAddress(address3);
    }
    if (neighborhood3 != null) {
      setNeighborhood(neighborhood3);
    }
    if (city3 != null) {
      setCity(city3);
    }
    if (country3 != null) {
      setCountry(country3);
    }
    if (status3 != null) {
      setStatus(status3);
    }
  }

  public void inactive() {
    setStatus(false);
  }

  @Override
  public String toString() {
    if (Hibernate.isInitialized(this)) {
      return "HairSalon [id=" + id + ", name=" + name + ", phone=" + phone + ", address=" + address + ", neighborhood="
          + neighborhood + ", city=" + city + ", country=" + country + ", status=" + status + "]";
    } else {
      return "Appointment (Proxy)";
    }
  }
}
