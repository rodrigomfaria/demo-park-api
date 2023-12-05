package com.rmf.demoparkapi.exceptions;

public class UsernameUniqueViolationException extends RuntimeException {

    public UsernameUniqueViolationException(String msg) {
        super(msg);
    }
}
