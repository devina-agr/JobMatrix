package com.example.jobmatrix.exception;

public class EmailAlreadyExistsException
        extends RuntimeException {

    public EmailAlreadyExistsException(
            String message
    ) {
        super(message);
    }
}