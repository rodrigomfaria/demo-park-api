package com.rmf.demoparkapi.exceptions;

import lombok.Getter;

@Getter
public class CodUniqueViolationException extends RuntimeException {

    private String resource;
    private String code;

    public CodUniqueViolationException(String message) {
        super(message);
    }

    public CodUniqueViolationException(String resource, String code) {

        this.resource = resource;
        this.code = code;
    }


}
