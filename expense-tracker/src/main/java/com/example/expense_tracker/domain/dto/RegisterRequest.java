package com.example.expense_tracker.domain.dto;

public record RegisterRequest(
        String firstName,
        String lastName,
        String email,
        String password
) {}
