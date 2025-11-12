package com.example.banking.exception;

import java.math.BigDecimal;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User not found with id: " + id);
    }

    public UserNotFoundException(String email) {
        super("User not found with email: " + email);
    }

    public UserNotFoundException(BigDecimal amount) {
        super("Users not found with balance over than : " + amount);
    }
}
