package com.example.expense_tracker.service;


import com.example.expense_tracker.domain.dto.AuthenticationRequest;
import com.example.expense_tracker.domain.dto.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    public AuthenticationResponse register(RequestBody request) {
        return null;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        return null;
    }
}
