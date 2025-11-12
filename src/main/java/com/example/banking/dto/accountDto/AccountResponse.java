package com.example.banking.dto.accountDto;

import java.math.BigDecimal;


public record AccountResponse (Long id, String accountNumber, BigDecimal amount) {
}
