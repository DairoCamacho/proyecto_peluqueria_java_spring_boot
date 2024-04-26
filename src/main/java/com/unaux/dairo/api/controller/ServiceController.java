package com.unaux.dairo.api.controller;

import com.unaux.dairo.api.domain.hairsalon.HairSalon;
import com.unaux.dairo.api.domain.service.*;
import com.unaux.dairo.api.repository.HairSalonRepository;
import com.unaux.dairo.api.repository.ServiceRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("api/service")
public class ServiceController {

  public final ServiceRepository serviceRepository;
  public final HairSalonRepository hairSalonRepository;

  public ServiceController(
    ServiceRepository serviceRepository,
    HairSalonRepository hairSalonRepository
  ) {
    this.serviceRepository = serviceRepository;
    this.hairSalonRepository = hairSalonRepository;
  }

  @PostMapping
  public ResponseEntity<ServiceResponseDto> createService(
    @RequestBody @Valid ServiceCreateDto serviceCreateDto,
    UriComponentsBuilder uriComponentsBuilder
  ) {
    // consultar el hairSalon
    HairSalon hairSalon = hairSalonRepository
      .findById(serviceCreateDto.hairSalonId())
      .orElseThrow(() -> new RuntimeException("HairSalon not found"));

    Service service = serviceRepository.save(
      new Service(serviceCreateDto, hairSalon)
    );

    ServiceResponseDto response = new ServiceResponseDto(
      service.getId(),
      service.getName(),
      service.getPrice(),
      service.getDuration(),
      service.getHairSalon().getId()
    );

    URI url = uriComponentsBuilder
      .path("api/service/{id}")
      .buildAndExpand(service.getId())
      .toUri();

    return ResponseEntity.created(url).body(response);
  }

  @GetMapping
  public ResponseEntity<Page<ServiceFindDto>> listAllService(
    Pageable pagination
  ) {
    return ResponseEntity.ok(
      serviceRepository.findAll(pagination).map(ServiceFindDto::new)
    );
  }

  @GetMapping("/{id}")
  public ResponseEntity<ServiceResponseDto> findService(@PathVariable int id) {
    Optional<Service> serviceOptional = serviceRepository.findById(id);

    if (!serviceOptional.isPresent()) {
      return ResponseEntity.notFound().build();
    }

    Service service = serviceOptional.get();

    ServiceResponseDto response = new ServiceResponseDto(
      service.getId(),
      service.getName(),
      service.getPrice(),
      service.getDuration(),
      service.getHairSalon().getId()
    );

    return ResponseEntity.ok(response);
  }


  @PutMapping
  @Transactional
  public ResponseEntity<ServiceResponseDto> findService(
    @RequestBody @Valid ServiceUpdateDto serviceUpdateDto
  ) {
    // consultar el Servicio
    Service service = serviceRepository
      .findById(serviceUpdateDto.id())
      .orElseThrow(() -> new RuntimeException("Service not found"));

    // consultar el hairSalon
    HairSalon hairSalon = hairSalonRepository.getReferenceById(
      serviceUpdateDto.hairSalonId()
    );

    // actualizamos
    service.update(serviceUpdateDto, hairSalon);

    // creamos el DTO para la respuesta
    ServiceResponseDto response = new ServiceResponseDto(
      service.getId(),
      service.getName(),
      service.getPrice(),
      service.getDuration(),
      service.getHairSalon().getId()
    );

    // enviamos la respuesta
    return ResponseEntity.ok(response);
  }

  // OJO: delete físico
  @DeleteMapping("/{id}")
  public void deleteService(@PathVariable int id) {
    Service service = serviceRepository
      .findById(id)
      .orElseThrow(() -> new RuntimeException("Service not found"));
    serviceRepository.delete(service);
  }
}
