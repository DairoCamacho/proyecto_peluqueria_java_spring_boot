package com.unaux.dairo.api.domain.client;

import java.time.LocalDate;

public record ClientFindDto(int id, String name, String lastName, String phone, LocalDate birthday, String email) {
    public ClientFindDto(Client client){
        this(client.getId(), client.getName(), client.getLastName(), client.getPhone(), client.getBirthday(), client.getEmail());
    }
}
