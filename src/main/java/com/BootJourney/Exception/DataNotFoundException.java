package com.BootJourney.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")
public class DataNotFoundException extends Throwable {
    private static final long serialVersionID = 1L;

    public DataNotFoundException(String message) {
        super(message);
    }

}
