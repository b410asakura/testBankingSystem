package com.example.banking.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserResponse(
        Long id,
        String name,
        String email,
        LocalDate registrationDate
) {
}
