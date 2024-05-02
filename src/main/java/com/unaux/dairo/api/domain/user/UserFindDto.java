package com.unaux.dairo.api.domain.user;

public record UserFindDto(int id, String email, Role role, boolean status) {
    public UserFindDto(User user){
        this(
            user.getId(),
            user.getEmail(),
            user.getRole(),
            user.isStatus()
        );
    }
}
