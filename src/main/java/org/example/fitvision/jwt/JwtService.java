package org.example.fitvision.jwt;

import io.jsonwebtoken.Claims;
import java.util.Map;
import java.util.function.Function;
import org.example.fitvision.jwt.dto.JwtUser;

public interface JwtService {
    String extractUsername(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    boolean isTokenValid(String token, JwtUser user);

    String generateToken(JwtUser user);

    String generateToken(Map<String, Object> extraClaims, JwtUser user);
}
