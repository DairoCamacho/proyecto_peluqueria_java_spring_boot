package com.unaux.dairo.api.infra.security;

import com.unaux.dairo.api.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticationService implements UserDetailsService {

  private final UserRepository userRepository;

  AutenticationService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  // establecemos de que forma vamos a cargar el usuario y de donde
  public UserDetails loadUserByUsername(String username)
    throws UsernameNotFoundException {
    return userRepository.findByEmail(username);
  }
}
