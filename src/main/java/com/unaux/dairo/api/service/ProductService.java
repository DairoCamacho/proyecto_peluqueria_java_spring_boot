package com.unaux.dairo.api.service;

import java.time.LocalTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.unaux.dairo.api.domain.hairsalon.HairSalon;
import com.unaux.dairo.api.domain.product.Product;
import com.unaux.dairo.api.infra.errors.ResourceNotFoundException;
import com.unaux.dairo.api.repository.ProductRepository;

@Service
public class ProductService {

  private final ProductRepository productRepository;
  private final HairSalonService hairSalonService;

  public ProductService(ProductRepository productRepository, HairSalonService hairSalonService) {
    this.productRepository = productRepository;
    this.hairSalonService = hairSalonService;
  }

  public Product save(String name, int price, LocalTime duration, int hairSalonId) {

    // consultar el hairSalon
    HairSalon hairSalon = hairSalonService
        .findById(hairSalonId)
        .orElseThrow(() -> new ResourceNotFoundException("HairSalon not found with the ID: " + hairSalonId));

    return productRepository.save(new Product(name, price, duration, hairSalon));
  }

  public Page<Product> findAll(Pageable pagination) {
    return productRepository.findAll(pagination);
  }

  public Optional<Product> findById(int id) {
    return productRepository.findById(id);
  }

  public Product update(int id, String name, int price, LocalTime duration, int hairSalonId, Boolean status) {

    // con el ID Buscamos la Entidad a actualizar
    Product product = productRepository.getReferenceById(id);

    // consultar el hairSalon
    HairSalon hairSalon = hairSalonService
        .findById(hairSalonId)
        .orElseThrow(() -> new ResourceNotFoundException("HairSalon not found"));
    // Actualizamos la Entidad con los datos del DTO
    product.update(name, price, duration, hairSalon, status);
    // retornamos la Entidad ya actualizada
    return product;
  }

  public void delete(int id) {
    // con el ID Buscamos la Entidad para desactivar
    Product product = productRepository.getReferenceById(id);
    // Desactivamos - borrado lógico
    product.inactivate();
  }
}
