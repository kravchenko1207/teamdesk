package com.example.teamdesk.common;

public record FieldValidationError(
        String field,
        String message
) {
}
