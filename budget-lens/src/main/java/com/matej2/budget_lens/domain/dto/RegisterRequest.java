package com.matej2.budget_lens.domain.dto;

public record RegisterRequest(
        String firstName,
        String lastName,
        String email,
        String password
) {}
