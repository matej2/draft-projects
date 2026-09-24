package com.matej2.budget_lens.domain.dto;


public record AuthenticationRequest (
        String email,
        String password
) {}