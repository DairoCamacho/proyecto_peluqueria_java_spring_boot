package com.unaux.dairo.api.domain.hairsalon;

public record HairSalonFindDto(
  int id,
  String name,
  String phone,
  String address,
  String neighborhood,
  String city,
  String country
) {
    public HairSalonFindDto(HairSalon hairSalon){
        this(
            hairSalon.getId(),
            hairSalon.getName(),
            hairSalon.getPhone(),
            hairSalon.getAddress(),
            hairSalon.getNeighborhood(),
            hairSalon.getCity(),
            hairSalon.getCountry()
        );
    }
}
