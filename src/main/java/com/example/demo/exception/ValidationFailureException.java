package com.example.demo.exception;

public class ValidationFailureException extends RuntimeException {
    private String message;

    public ValidationFailureException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
