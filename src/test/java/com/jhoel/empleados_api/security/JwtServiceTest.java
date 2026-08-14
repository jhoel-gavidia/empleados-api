package com.jhoel.empleados_api.security;

import com.jhoel.empleados_api.entity.Role;
import com.jhoel.empleados_api.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    private final String secret =
            "my-super-secret-key-for-jwt-testing-which-is-long-enough";

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        ReflectionTestUtils.setField(
                jwtService,
                "secretKey",
                secret
        );

        ReflectionTestUtils.setField(
                jwtService,
                "expiration",
                3600000L
        );
    }

    private User createUser() {
        return User.builder()
                .id(1L)
                .username("admin")
                .email("admin@email.com")
                .password("password")
                .role(Role.ADMIN)
                .build();
    }

    @Test
    void generateToken_shouldGenerateToken() {

        User user = createUser();

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void extractUsername_shouldReturnUsername() {

        User user = createUser();

        String token = jwtService.generateToken(user);

        String username = jwtService.extractUsername(token);

        assertEquals("admin", username);
    }

    @Test
    void isTokenValid_shouldReturnTrueForValidToken() {

        User user = createUser();

        String token = jwtService.generateToken(user);

        boolean valid = jwtService.isTokenValid(token, user);

        assertTrue(valid);
    }

    @Test
    void isTokenValid_shouldReturnFalseForDifferentUser() {

        User user = createUser();

        String token = jwtService.generateToken(user);

        User differentUser = User.builder()
                .username("user")
                .email("user@email.com")
                .password("password")
                .role(Role.USER)
                .build();

        boolean valid =
                jwtService.isTokenValid(token, differentUser);

        assertFalse(valid);
    }
}