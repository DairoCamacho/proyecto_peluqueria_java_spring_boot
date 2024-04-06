package com.unaux.dairo.api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.unaux.dairo.api.repository.UserRepository;

@Service
public class AutenticationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    // establecemos de que forma vamos a cargar el usuario y de donde
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }
}
