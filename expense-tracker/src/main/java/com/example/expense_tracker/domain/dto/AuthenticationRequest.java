package com.example.expense_tracker.domain.dto;


public record AuthenticationRequest (
        String email,
        String password
) {}