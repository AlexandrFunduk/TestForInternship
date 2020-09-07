package com.space.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Not valid ship parameters")
public class ShipValidationException extends RuntimeException {
    private String message;

    public ShipValidationException(String message) {
    }

    public String getMessage() {
        return message;
    }
}
