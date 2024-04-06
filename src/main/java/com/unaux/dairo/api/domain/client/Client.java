package com.unaux.dairo.api.domain.client;

import com.unaux.dairo.api.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="client")
@PrimaryKeyJoinColumn
public class Client extends User {
    private String type;


    public Client(ClientCreateDto clientCreateDto) {
        setStatus(true);
        setName(clientCreateDto.name());
        setLastName(clientCreateDto.lastName());
        setPhone(clientCreateDto.phone());
        setBirthday(LocalDate.parse(clientCreateDto.birthday()));
        setEmail(clientCreateDto.email());
        setPassword(clientCreateDto.password());
        setType("new");// por defecto todos serán nuevos
    }

    public void update(ClientUpdateDto clientUpdateDto) {
        if (clientUpdateDto.name() != null) {
            setName(clientUpdateDto.name());
        }
        if (clientUpdateDto.lastName() != null) {
            setLastName(clientUpdateDto.lastName());
        }
        if (clientUpdateDto.phone() != null) {
            setPhone(clientUpdateDto.phone());
        }
        if (clientUpdateDto.birthday() != null) {
            setBirthday(LocalDate.parse(clientUpdateDto.birthday()));
        }
        if (clientUpdateDto.type() != null) {
            setType(clientUpdateDto.type());
        }
        if (clientUpdateDto.email() != null) {
            setEmail(clientUpdateDto.email());
        }
    }

    public void inactivate() {
        setStatus(false);
    }
}