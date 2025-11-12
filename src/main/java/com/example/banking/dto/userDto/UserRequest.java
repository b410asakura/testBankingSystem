package com.example.banking.dto.userDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest (
        @NotBlank(message = "Name is required")
        String name,
        @Email(message = "invalid email format")
        @NotBlank(message = "email is required")
        String email
){
}
