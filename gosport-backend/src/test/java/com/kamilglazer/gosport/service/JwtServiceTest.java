package com.kamilglazer.gosport.service;


import com.kamilglazer.gosport.config.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import java.lang.reflect.Field;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import static org.assertj.core.api.Assertions.*;

public class JwtServiceTest {

    private JwtService jwtService;
    private final String SECRET_KEY = "dGVzdC1zZWNyZXQta2V5LXRlc3Qtc2VjcmV0LWtleS10ZXN0LXNlY3JldC1rZXk";

    @BeforeEach
    void setUp() throws Exception {
        jwtService = new JwtService();

        Field secretKeyField = JwtService.class.getDeclaredField("SECRET_KEY");
        secretKeyField.setAccessible(true);
        secretKeyField.set(jwtService, SECRET_KEY);
    }

    private UserDetails getTestUser() {
        return new User("john.doe@example.com","password", Collections.emptyList());
    }

    @Test
    void shouldGenerateAndValidToken() {
        UserDetails testUser = getTestUser();
        String token = jwtService.generateToken(testUser);

        assertThat(token).isNotEmpty();
        assertThat(jwtService.isTokenValid(token,testUser)).isTrue();
        assertThat(jwtService.extractUsername(token)).isEqualTo(testUser.getUsername());
    }

    @Test
    void shouldReturnFalseForInvalidUser() {
        UserDetails testUser = getTestUser();
        String token = jwtService.generateToken(testUser);

        UserDetails otherUser = new User("other@example.com","password", Collections.emptyList());

        assertThat(jwtService.isTokenValid(token,otherUser)).isFalse();
    }

    @Test
    void shouldExtractClaims() {
        UserDetails testUser = getTestUser();
        String token = jwtService.generateToken(testUser);

        Date expiration = jwtService.extractClaim(token, Claims::getExpiration);
        assertThat(expiration).isAfter(new Date());
    }

    @Test
    void shouldExtractTokenFromHeader() {
        String header = "Bearer abc.def.ghi";
        assertThat(jwtService.getToken(header)).isEqualTo("abc.def.ghi");
    }

    @Test
    void shouldReturnFalseWhenTokenExpired() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        String expiredToken = Jwts.builder()
                .setSubject("john.doe@example.com")
                .setIssuedAt(new Date(System.currentTimeMillis() - 100000))
                .setExpiration(new Date(System.currentTimeMillis() - 100000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        UserDetails testUser = getTestUser();

        assertThatThrownBy(() -> jwtService.isTokenValid(expiredToken,testUser)).isInstanceOf(ExpiredJwtException.class);
    }
}
