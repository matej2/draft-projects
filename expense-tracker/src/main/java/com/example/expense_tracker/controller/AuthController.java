package com.example.expense_tracker.controller;

import com.example.expense_tracker.domain.dto.AuthenticationRequest;
import com.example.expense_tracker.domain.dto.AuthenticationResponse;
import com.example.expense_tracker.domain.dto.RegisterRequest;
import com.example.expense_tracker.service.AuthenticationService;
import com.example.expense_tracker.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final TokenService tokenservice;
    private final AuthenticationService authService;


    @PostMapping("/register")
    public HttpStatus register(
            @RequestBody RegisterRequest request
    ) {
        authService.register(request);
        return HttpStatus.OK;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authService.authenticate(request));
    }


    @PostMapping("/token")
    public String token(Authentication authentication) {
        return tokenservice.generateToken(authentication);
    }
}
