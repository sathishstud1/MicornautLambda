package com.example.exceptions;

public class BadInputException extends RuntimeException {
    public BadInputException(String s) {
        super(s);
    }
}
