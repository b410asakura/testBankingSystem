package com.example.banking.dto.errorDto;

import java.time.LocalDateTime;

public record ErrorResponse (LocalDateTime timestamp, int Status, String message, String error) {
}
