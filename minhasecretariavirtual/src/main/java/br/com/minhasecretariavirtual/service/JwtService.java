package br.com.minhasecretariavirtual.service;

import br.com.minhasecretariavirtual.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(
                        Keys.hmacShaKeyFor(
                                jwtProperties.getSecret().getBytes()
                        )
                )
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}