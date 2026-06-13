package com.example.jobmatrix.security;

import com.example.jobmatrix.dto.TokenPayload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final String secretKey;

    private final long accessTokenExpiration;

    private final long refreshTokenExpiration;

    public JwtService(
            @Value("${jwt.secret}") String secretKey,

            @Value("${jwt.access-token-expiration}")
            long accessTokenExpiration,

            @Value("${jwt.refresh-token-expiration}")
            long refreshTokenExpiration
    ) {

        this.secretKey = secretKey;
        this.accessTokenExpiration =
                accessTokenExpiration;

        this.refreshTokenExpiration =
                refreshTokenExpiration;
    }

    private Key getSigningKey() {

        return Keys.hmacShaKeyFor(
                secretKey.getBytes()
        );
    }

    private Claims extractClaims(
            String token
    ) {

        return Jwts.parser()
                .verifyWith(
                        (SecretKey) getSigningKey()
                )
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractEmail(
            String token
    ) {

        return extractClaims(token)
                .getSubject();
    }

    public Integer extractTokenVersion(
            String token
    ) {

        return extractClaims(token)
                .get(
                        "tokenVersion",
                        Integer.class
                );
    }

    public boolean validateToken(
            String token,
            TokenPayload payload
    ) {

        return extractEmail(token)
                .equals(payload.email())

                && extractTokenVersion(token)
                .equals(payload.tokenVersion())

                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(
            String token
    ) {

        return extractClaims(token)
                .getExpiration()
                .before(new Date());
    }

    private String generateToken(
            TokenPayload payload,
            long expiration
    ) {

        return Jwts.builder()

                .subject(
                        payload.email()
                )

                .claim(
                        "tokenVersion",
                        payload.tokenVersion()
                )

                .issuedAt(
                        new Date()
                )

                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + expiration
                        )
                )

                .signWith(
                        getSigningKey()
                )

                .compact();
    }

    public String generateAccessToken(
            TokenPayload payload
    ) {

        return generateToken(
                payload,
                accessTokenExpiration
        );
    }

    public String generateRefreshToken(
            TokenPayload payload
    ) {

        return generateToken(
                payload,
                refreshTokenExpiration
        );
    }
}