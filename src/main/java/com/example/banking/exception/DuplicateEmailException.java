package com.example.banking.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException() {
        super("Почта с таким названием уже существует");
    }
}
