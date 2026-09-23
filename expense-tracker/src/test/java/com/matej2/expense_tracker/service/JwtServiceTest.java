package com.matej2.expense_tracker.service;

import com.matej2.expense_tracker.config.RsaKeyProperties;
import com.matej2.expense_tracker.domain.entity.User;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HexFormat;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {
    @Mock
    RSAPublicKey publicKey;
    @Mock
    RSAPrivateKey privateKey;

    private RsaKeyProperties rsaKeyProperties;

    private JwtService jwtServiceUnderTest;
    private User user;
    private User anotherUser;
    private String validToken;
    private String differentToken;

    @BeforeEach
    public void setUp() {
        rsaKeyProperties = new RsaKeyProperties(publicKey, privateKey);

        jwtServiceUnderTest = new  JwtService(rsaKeyProperties);

        this.user = new User();
        this.user.setEmail("test@mail.com");
        this.anotherUser = new User();
        this.anotherUser.setEmail("anotheruser@mail.com");

        String privateKey = "e04fd020ea3a6910a2d808002b30309de04fd020ea3a6910a2d808002b30309de04fd020ea3a6910a2d808002b30309d";

        when(rsaKeyProperties.privateKey().getEncoded()).thenReturn(HexFormat.of().parseHex(privateKey));

        this.validToken = jwtServiceUnderTest.generateToken(user);
        this.differentToken = jwtServiceUnderTest.generateToken(anotherUser);
    }

    @Test
    void testExtractUsernameForValidJwt() {
        String expectedUsername = "test@mail.com";

        assertThat(jwtServiceUnderTest.extractUsername(validToken)).isEqualTo(expectedUsername);
    }

    @Test
    void testGenerateToken() {
        String generatedToken = jwtServiceUnderTest.generateToken(user);

        assertThat(generatedToken).isNotNull();
        assertThat(generatedToken).isNotEmpty();
    }

    @Test
    void  testIsTokenValidForValidToken() {
        assertThat(jwtServiceUnderTest.isTokenValid(this.validToken, user)).isTrue();
    }

    @Test
    void testIsTokenValidForInvalidToken() {
        // Scenario 1: Random token
        String invalidToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QG1haWhuY29tIiwiaWF0IjoxNzkwMTYyNDYwLCJleHAiOjE3OTAxNgM5MDB9.8V0Qj3xqCWOAvurvXD6JLHwVHPpo8hOXLsDf7a5ciK8";

        SignatureException exception = assertThrows(SignatureException.class, () -> jwtServiceUnderTest.isTokenValid(invalidToken, user));
        assertEquals("JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.", exception.getMessage());

        // Scenario 2: username does not match
        boolean isValid = jwtServiceUnderTest.isTokenValid(differentToken, user);
        assertThat(isValid).isFalse();
    }
}
