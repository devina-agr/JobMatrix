package com.example.jobmatrix.exception;

public class UnauthorizedException
        extends RuntimeException {

    public UnauthorizedException(
            String message
    ) {
        super(message);
    }
}