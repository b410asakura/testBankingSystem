package com.example.banking.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String email) {
        super("Почта с таким названием уже существует: " + email);
    }
}
