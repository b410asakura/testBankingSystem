package com.example.banking.exception;

public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(String email){
        super("Account not found with email: " + email);
    }

    public AccountNotFoundException(Long id) {
        super("Account not found with id: " + id);
    }
}
