package com.example.jobmatrix.exception;

public class BadRequestException
        extends RuntimeException {

    public BadRequestException(
            String message
    ) {
        super(message);
    }
}