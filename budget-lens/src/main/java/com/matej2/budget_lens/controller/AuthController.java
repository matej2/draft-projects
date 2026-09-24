package com.matej2.budget_lens.controller;

import com.matej2.budget_lens.domain.dto.AuthenticationRequest;
import com.matej2.budget_lens.domain.dto.AuthenticationResponse;
import com.matej2.budget_lens.domain.dto.RegisterRequest;
import com.matej2.budget_lens.service.AuthenticationService;
import com.matej2.budget_lens.service.TokenService;
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
