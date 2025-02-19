package org.jset.services;

import io.smallrye.jwt.auth.principal.DefaultJWTParser;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jset.dtos.ClipRequestDTO;
import org.jset.dtos.UserDTO;
import org.jset.constants.Roles;

import java.time.Duration;
import java.util.*;

@ApplicationScoped
public class TokenService {

    @ConfigProperty(name = "com.jset.jwt.duration")
    long tokenDuration;

    @ConfigProperty(name = "com.jset.jwt.refresh.duration")
    long refreshTokenDuration;

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    public String generateToken(UserDTO user) {
        return Jwt.issuer(issuer)
                .upn(user.email())
                .groups(new HashSet<>(Collections.singletonList(user.role())))
                .expiresIn(Duration.ofSeconds(tokenDuration))
                .claim("role", user.role())
                .sign();
    }

    public String generateRefreshToken(UserDTO user) {
        return Jwt.issuer(issuer)
                .upn(user.email())
                .groups(new HashSet<>(List.of("refresh")))
                .expiresIn(Duration.ofSeconds(refreshTokenDuration))
                .claim("role", user.role())
                .sign();
    }

    public Optional<String> validateRefreshToken(String refreshToken) {
        try {
            JWTParser parser = new DefaultJWTParser();
            var jwt = parser.parse(refreshToken);
            if (jwt != null && jwt.getGroups().contains("refresh")) {
                return Optional.of(jwt.getName());
            }
            return Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
