package co.edu.uniquindio.quindioflix.configuration.security;

import co.edu.uniquindio.quindioflix.business.model.EstadoCuenta;
import co.edu.uniquindio.quindioflix.business.model.RolUsuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private static final String CLAIM_USUARIO_ID = "usuarioId";
    private static final String CLAIM_ROL = "rol";
    private static final String CLAIM_ESTADO_CUENTA = "estadoCuenta";

    private final SecretKey key;
    private final long expirationMinutes;

    public JwtService(
            @Value("${app.security.jwt-secret}") String secret,
            @Value("${app.security.jwt-expiration-minutes}") long expirationMinutes
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMinutes = expirationMinutes;
    }

    public String generate(UserDetails user, Map<String, Object> claims) {
        Instant now = Instant.now();

        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(expirationMinutes * 60)))
                .signWith(key)
                .compact();
    }

    public String username(String token) {
        return claims(token).getSubject();
    }

    public Long usuarioId(String token) {
        Object value = claims(token).get(CLAIM_USUARIO_ID);

        if (value instanceof Number number) {
            return number.longValue();
        }

        return Long.valueOf(String.valueOf(value));
    }

    public RolUsuario rol(String token) {
        return RolUsuario.valueOf(String.valueOf(claims(token).get(CLAIM_ROL)));
    }

    public EstadoCuenta estadoCuenta(String token) {
        return EstadoCuenta.valueOf(String.valueOf(claims(token).get(CLAIM_ESTADO_CUENTA)));
    }

    public boolean valid(String token) {
        return claims(token).getExpiration().after(new Date());
    }

    public boolean valid(String token, UserDetails user) {
        return username(token).equals(user.getUsername()) && valid(token);
    }

    private Claims claims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
