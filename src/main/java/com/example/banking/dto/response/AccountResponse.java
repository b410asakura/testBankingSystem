package com.example.banking.dto.response;

import java.math.BigDecimal;


public record AccountResponse (Long id, String accountNumber, BigDecimal amount) {
}
