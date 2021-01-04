package com.example.exceptions;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class ApiErrorMessage {
    private String message;

    public ApiErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
