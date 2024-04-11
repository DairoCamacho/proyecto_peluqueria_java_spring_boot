package com.unaux.dairo.api.controller;

import com.unaux.dairo.api.domain.hairsalon.HairSalon;
import com.unaux.dairo.api.domain.hairsalon.HairSalonCreateDto;
import com.unaux.dairo.api.domain.hairsalon.HairSalonFindDto;
import com.unaux.dairo.api.domain.hairsalon.HairSalonResponseDto;
import com.unaux.dairo.api.repository.HairSalonRepository;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("api/hairsalon")
public class HairSalonController {

  private final HairSalonRepository hairSalonRepository;

  HairSalonController(HairSalonRepository hairSalonRepository) {
    this.hairSalonRepository = hairSalonRepository;
  }

  @PostMapping
  public ResponseEntity createHairSalon(
    @RequestBody @Valid HairSalonCreateDto hairSalonCreateDto,
    UriComponentsBuilder uriComponentsBuilder
  ) {
    // Aquí creamos una objeto hairsalon pasándole el DTO, este resultado
    // lo pasamos al método save de repository
    HairSalon hairSalon = hairSalonRepository.save(
      new HairSalon(hairSalonCreateDto)
    );
    // creamos un DTO para retornar el objeto creado al frontend
    HairSalonResponseDto response = new HairSalonResponseDto(
      hairSalon.getId(),
      hairSalon.getName(),
      hairSalon.getPhone(),
      hairSalon.getAddress(),
      hairSalon.getNeighborhood(),
      hairSalon.getCity(),
      hairSalon.getCountry()
    );

    // Aquí crearemos una url que corresponde al objeto
    // que se creó en la base de datos.
    URI url = uriComponentsBuilder
      .path("api/hairsalon/{id}")
      .buildAndExpand(hairSalon.getId())
      .toUri();

    return ResponseEntity.created(url).body(response);
  }

  @GetMapping
  public ResponseEntity<Page<HairSalonFindDto>> listHairSalon(
    Pageable paginacion // Parámetro para recibir la paginación de la petición del frontend
    ) {
    //
    return ResponseEntity.ok(
      hairSalonRepository
      .findAll(paginacion) // Obtenemos todos los registros de la base de datos
      .map(HairSalonFindDto::new) // Mapeamos los registros a un DTO
    );
  }

  @GetMapping("/{id}")
  public ResponseEntity<HairSalonResponseDto> findHairSalon(@PathVariable int id){
    // Aquí obtenemos una referencia del objeto en la base de datos
    HairSalon hairSalon = hairSalonRepository.getReferenceById(id);
    // Creamos un DTO para retornar el objeto al frontend
    HairSalonResponseDto response = new HairSalonResponseDto(
      hairSalon.getId(),
      hairSalon.getName(),
      hairSalon.getPhone(),
      hairSalon.getAddress(),
      hairSalon.getNeighborhood(),
      hairSalon.getCity(),
      hairSalon.getCountry()
    );
    return ResponseEntity.ok(response);
  }
}
