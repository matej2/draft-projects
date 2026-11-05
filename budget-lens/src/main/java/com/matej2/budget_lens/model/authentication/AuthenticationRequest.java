package com.matej2.budget_lens.model.authentication;


public record AuthenticationRequest (
        String email,
        String password
) {}