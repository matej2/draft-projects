package com.matej2.expense_tracker.domain.dto;


public record AuthenticationRequest (
        String email,
        String password
) {}