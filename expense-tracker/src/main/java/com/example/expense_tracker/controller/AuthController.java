package com.example.expense_tracker.controller;

import com.example.expense_tracker.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger LOG  = LoggerFactory.getLogger(AuthController.class);
    private final TokenService tokenservice;

    public AuthController(TokenService tokenservice) {
        this.tokenservice = tokenservice;
    }

    @PostMapping("/token")
    public String token(Authentication authentication) {
        LOG.debug("Token request for user: {}", authentication.getName());
        return tokenservice.generateToken(authentication);
    }
}
