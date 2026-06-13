package com.example.jobmatrix.exception;

public class RefreshTokenExpiredException
        extends RuntimeException {

    public RefreshTokenExpiredException(
            String message
    ) {
        super(message);
    }
}