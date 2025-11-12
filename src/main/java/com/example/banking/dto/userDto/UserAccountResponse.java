package com.example.banking.dto.userDto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserAccountResponse(
        Long id,
        String name,
        String email,
        LocalDate registrationDate,
        String accountNumber,
        BigDecimal amount
) {
}
