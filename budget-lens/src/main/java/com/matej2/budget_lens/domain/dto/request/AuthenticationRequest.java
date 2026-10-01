package com.matej2.budget_lens.domain.dto.request;


public record AuthenticationRequest (
        String email,
        String password
) {}