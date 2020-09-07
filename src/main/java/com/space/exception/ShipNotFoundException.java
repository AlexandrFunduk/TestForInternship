package com.space.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Ship not found")
public class ShipNotFoundException extends RuntimeException {
    private String message;
    public ShipNotFoundException(String message) {
    }

    public String getMessage() {
        return message;
    }
}
