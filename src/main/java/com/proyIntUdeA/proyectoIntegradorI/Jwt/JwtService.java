package com.proyIntUdeA.proyectoIntegradorI.Jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.proyIntUdeA.proyectoIntegradorI.entity.PersonEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    // Inyectar la clave secreta desde application.properties
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public String getToken(UserDetails user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("user_email", ((PersonEntity) user).getUserEmail());
        extraClaims.put("user_id", ((PersonEntity) user).getUserId());
        extraClaims.put("user_lastname", ((PersonEntity) user).getUserLastname());
        extraClaims.put("user_role", ((PersonEntity) user).getUserRole());
        extraClaims.put("user_state", ((PersonEntity) user).getUserState());

        return getToken(extraClaims, user);
    }

    private String getToken(Map<String, Object> extraClaims, UserDetails user) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // 24 minutos
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token) {
        Claims claims = getAllClaims(token);
        return claims.get("user_email", String.class);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Claims getAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public ResponseEntity<String> verifyToken(HttpServletRequest request) {
        // Validar encabezado de autorización
        String authHeader = request.getHeader("Authorization");
        if (isInvalidAuthHeader(authHeader)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Token missing or invalid");
        }

        // Extraer token
        String token = extractToken(authHeader);

        try {
            // Validar y parsear token
            validateToken(token);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("El token es correcto");
        } catch (TokenValidationException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }
    }

    private boolean isInvalidAuthHeader(String authHeader) {
        return authHeader == null || !authHeader.startsWith("Bearer ");
    }

    private String extractToken(String authHeader) {
        return authHeader.substring(7);
    }

    private void validateToken(String token) throws TokenValidationException {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception e) {
            throw new TokenValidationException("Invalid token");
        }
    }

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    // Excepción personalizada para validación de token
    public static class TokenValidationException extends Exception {
        public TokenValidationException(String message) {
            super(message);
        }
    }
}