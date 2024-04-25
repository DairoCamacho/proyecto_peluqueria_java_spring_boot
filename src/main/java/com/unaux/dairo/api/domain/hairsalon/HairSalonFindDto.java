package com.unaux.dairo.api.domain.hairsalon;

public record HairSalonFindDto(
  int id,
  String address,
  String city,
  String country,
  String name,
  String neighborhood,
  String phone,
  boolean status
) {
    public HairSalonFindDto(HairSalon hairSalon){
        this(
            hairSalon.getId(),
            hairSalon.getAddress(),
            hairSalon.getCity(),
            hairSalon.getCountry(),
            hairSalon.getName(),
            hairSalon.getNeighborhood(),
            hairSalon.getPhone(),
            hairSalon.isStatus()
        );
    }
}
