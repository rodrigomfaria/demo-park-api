package com.rmf.demoparkapi.exceptions;

public class CodUniqueViolationException extends RuntimeException {
    public CodUniqueViolationException(String message) {
        super(message);
    }
}
