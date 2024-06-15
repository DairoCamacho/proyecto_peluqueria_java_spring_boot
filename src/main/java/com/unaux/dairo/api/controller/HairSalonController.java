package com.unaux.dairo.api.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

import com.unaux.dairo.api.domain.hairsalon.HairSalon;
import com.unaux.dairo.api.domain.hairsalon.HairSalonCreateDto;
import com.unaux.dairo.api.domain.hairsalon.HairSalonFindDto;
import com.unaux.dairo.api.domain.hairsalon.HairSalonResponseDto;
import com.unaux.dairo.api.domain.hairsalon.HairSalonUpdateDto;
import com.unaux.dairo.api.service.HairSalonService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/hairsalon")
// @PreAuthorize("hasRole('ADMIN')")
// @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class HairSalonController {
  // private static final Logger logger = LogManager.getLogger(ClientService.class);
  private final HairSalonService hairSalonService;

  HairSalonController(HairSalonService hairSalonService) {
    this.hairSalonService = hairSalonService;
  }

  @PostMapping
  public ResponseEntity<?> createHairSalon(UriComponentsBuilder uriComponentsBuilder,
      @RequestBody @Valid HairSalonCreateDto hairSalonCreateDto) {
    // Extraer los Datos
    String name = hairSalonCreateDto.name();
    String phone = hairSalonCreateDto.phone();
    String address = hairSalonCreateDto.address();
    String neighborhood = hairSalonCreateDto.neighborhood();
    String city = hairSalonCreateDto.city();
    String country = hairSalonCreateDto.country();

    // *** validaciones menores (tipo, formato, etc)
    if (!validatePhone(hairSalonCreateDto.phone())) {
      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .header("Error-Phone", "Phone cannot be less that 10 characters")
          .body("Phone cannot be less that 10 characters"); // remplazable por .build();
    }

    HairSalon hairSalon = hairSalonService.save(name, phone, address, neighborhood, city, country);
    // creamos un DTO para retornar el objeto creado al frontend
    HairSalonResponseDto response = mapHairSalonToResponseDto(hairSalon);
    // Aquí crearemos una url que corresponde al objeto que se creó en la base de datos.
    URI url = uriComponentsBuilder
        .path("api/hairsalon/{id}")
        .buildAndExpand(hairSalon.getId())
        .toUri();

    return ResponseEntity.created(url).body(response);
  }

  @GetMapping
  public ResponseEntity<Page<HairSalonFindDto>> listAllHairSalon(Pageable pagination) {
    Page<HairSalon> listHairSalons = hairSalonService.findAll(pagination); // Obtenemos todos los registros de la base de datos
    return ResponseEntity.ok(listHairSalons.map(HairSalonFindDto::new)); // Mapeamos los registros a un DTO
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> findHairSalon(@PathVariable int id) {
    // *** No hay validaciones menores para realizar
    Optional<HairSalon> hairSalonOptional = hairSalonService.findById(id);

    if (hairSalonOptional.isEmpty()) {

      Map<String, Object> errorDetails = new HashMap<>();
      errorDetails.put("code", "RESOURCE_NOT_FOUND");
      errorDetails.put("message", "The requested resource was not found");
      errorDetails.put("details", "No record with the ID " + id + " was found in the database.");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);

      // Enviar un código de estado HTTP 404 Not Found con un mensaje en el cuerpo
      //! return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The requested resource was not found");
    }
    // Creamos un DTO para retornar el objeto al frontend
    HairSalon hairSalon = hairSalonOptional.get();
    HairSalonResponseDto response = mapHairSalonToResponseDto(hairSalon);

    return ResponseEntity.ok(response);
  }

  @PutMapping
  @Transactional
  public ResponseEntity<?> updateHairSalon(
      @RequestBody @Valid HairSalonUpdateDto hairSalonUpdateDto) {
    // Extraer los Datos
    int id = hairSalonUpdateDto.id();
    String name = hairSalonUpdateDto.name();
    String phone = hairSalonUpdateDto.phone();
    String address = hairSalonUpdateDto.address();
    String neighborhood = hairSalonUpdateDto.neighborhood();
    String city = hairSalonUpdateDto.city();
    String country = hairSalonUpdateDto.country();
    Boolean status = hairSalonUpdateDto.status();

    try {
      HairSalon hairSalon = hairSalonService.update(id, name, phone, address, neighborhood, city, country, status);
      // Creamos un DTO para retornar el objeto al frontend
      HairSalonResponseDto response = mapHairSalonToResponseDto(hairSalon);

      return ResponseEntity.ok(response);
    } catch (EntityNotFoundException e) {
      String errorMessage = "Resource not found with ID: %d".formatted(hairSalonUpdateDto.id());
      return ResponseEntity.badRequest().body(errorMessage);
    }
  }

  @DeleteMapping("/{id}")
  @Transactional
  public ResponseEntity<?> deleteHairSalon(@PathVariable int id) {
    try {
      hairSalonService.delete(id);
      // Retornamos una respuesta vacía
      return ResponseEntity.noContent().build();
    } catch (EntityNotFoundException e) {
      String errorMessage = "Resource not found with ID: %d".formatted(id);
      return ResponseEntity.badRequest().body(errorMessage);
    }
  }

  public boolean validatePhone(String phone) {
    /*
    String regex = "^[+]*[(]{0,3}[0-9]{1,4}[)]{0,3}[0-9]{5,15}$"; // Phone validation regex
    
    Explicación del patrón: "+1 (212) 555-1212"
    ^: Indica el inicio de la cadena.
    [+]*: Coincide con cero o más signos de más (+).
    [(]{0,3}: Coincide con cero a tres paréntesis de apertura ([).
    [0-9]{1,4}: Coincide con uno a cuatro dígitos del 0 al 9.
    [)]{0,3}: Coincide con cero a tres paréntesis de cierre (]).
    [0-9]{5,15}: Coincide con cinco a quince dígitos del 0 al 9.
    $: Indica el final de la cadena.
    
    return Pattern.matches(regex, phone);
     */
    return (phone.length() >= 10);
  }

  private HairSalonResponseDto mapHairSalonToResponseDto(HairSalon hairSalon) {
    return new HairSalonResponseDto(
        hairSalon.getId(),
        hairSalon.getName(),
        hairSalon.getPhone(),
        hairSalon.getAddress(),
        hairSalon.getNeighborhood(),
        hairSalon.getCity(),
        hairSalon.getCountry(),
        hairSalon.isStatus());
  }
}
