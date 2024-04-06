package com.unaux.dairo.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.unaux.dairo.api.domain.user.User;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

  @Value("${api.security.secret}")
  private String apiSecret;

  public String createToken(User user) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(apiSecret);
      return JWT
        .create()
        .withIssuer("App Peluquería")
        .withSubject(user.getEmail())
        .withClaim("id", user.getId())
        .withExpiresAt(generateExpirationDate())
        .sign(algorithm);
    } catch (JWTCreationException exception) {
      throw new RuntimeException();
    }
  }

  private Instant generateExpirationDate() {
    return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-05:00"));
  }

  public String getSubject(String token) {
    if (token == null) {
      throw new RuntimeException("el token es nulo");
    }
    DecodedJWT verifier = null;
    try {
      Algorithm algorithm = Algorithm.HMAC256(apiSecret);
      verifier =
        JWT
          .require(algorithm)
          .withIssuer("App Peluquería")
          .build()
          .verify(token);
      verifier.getSubject();
    } catch (JWTVerificationException exception) {
      throw new RuntimeException(
        "Error en el método 'getSubject' durante la validación del token"
      );
    }
    if (verifier.getSubject() == null) {
      throw new RuntimeException("Verifier invalido");
    }
    return verifier.getSubject();
  }
}
