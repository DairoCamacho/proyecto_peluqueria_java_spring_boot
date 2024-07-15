package com.unaux.dairo.api.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.unaux.dairo.api.domain.hairsalon.HairSalon;
import com.unaux.dairo.api.repository.HairSalonRepository;

@Service
public class HairSalonService {

  private final HairSalonRepository hairSalonRepository;

  public HairSalonService(HairSalonRepository hairSalonRepository) {
    this.hairSalonRepository = hairSalonRepository;
  }

  public HairSalon save(String name, String phone, String address, String neighborhood, String city, String country) {
    return hairSalonRepository.save(new HairSalon(name, phone, address, neighborhood, city, country));
  }

  public Page<HairSalon> findAll(Pageable pagination) {
    return hairSalonRepository.findAll(pagination);
  }

  public Page<HairSalon> findEnabled(Pageable pagination) {
    return hairSalonRepository.findByStatusTrue(pagination);
  }

  public Optional<HairSalon> findById(int id) {
    return hairSalonRepository.findById(id);
  }

  public HairSalon update(int id, String name, String phone, String address, String neighborhood, String city,
      String country, Boolean status) {
    // con el ID Buscamos la Entidad a actualizar
    HairSalon hairSalon = hairSalonRepository.getReferenceById(id);
    // Actualizamos la Entidad con los datos del DTO
    hairSalon.update(name, phone, address, neighborhood, city, country, status);
    // retornamos la Entidad ya actualizada
    return hairSalon;
  }

  public void delete(int id) {
    // con el ID Buscamos la Entidad para desactivar
    HairSalon hairSalon = hairSalonRepository.getReferenceById(id);
    // Desactivamos - borrado lógico
    hairSalon.inactive();
  }
}
