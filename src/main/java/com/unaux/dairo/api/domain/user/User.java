package com.unaux.dairo.api.domain.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
@Entity (name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Column(name = "last_name")
    private String lastName;
    private LocalDate birthday;
    private String phone;
    private String email;
    private String password;
    private boolean status;
    @Transient // no se mapea para no crearlo en la DB
    private String confirmPassword;
}
