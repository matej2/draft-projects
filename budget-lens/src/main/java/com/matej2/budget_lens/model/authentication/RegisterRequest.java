package com.matej2.budget_lens.model.authentication;

public record RegisterRequest(
        String firstName,
        String lastName,
        String email,
        String password
) {}
