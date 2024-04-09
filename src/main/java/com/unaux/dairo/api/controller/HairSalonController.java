package com.unaux.dairo.api.controller;

import com.unaux.dairo.api.domain.hairsalon.HairSalonCreateDto;
import com.unaux.dairo.api.repository.HairSalonRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/hairsalon")
public class HairSalonController {

  private final HairSalonRepository hairSalonRepository;

  HairSalonController(HairSalonRepository hairSalonRepository) {
    this.hairSalonRepository = hairSalonRepository;
  }

  @PostMapping
  public ResponseEntity createHairSalon(@RequestBody HairSalonCreateDto hairSalonCreateDto){
    
  }
}
