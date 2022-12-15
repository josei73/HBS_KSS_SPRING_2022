package de.hsbremen.mkss.restservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ItemNotFound extends Exception {
    public ItemNotFound(String message) {
        super(message);
    }
}
