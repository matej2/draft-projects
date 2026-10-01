package com.matej2.budget_lens.domain.dto.request;

public record RegisterRequest(
        String firstName,
        String lastName,
        String email,
        String password
) {}
