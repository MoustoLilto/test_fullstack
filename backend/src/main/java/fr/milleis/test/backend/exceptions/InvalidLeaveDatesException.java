package fr.milleis.test.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidLeaveDatesException extends RuntimeException {
    
    public InvalidLeaveDatesException(String message) {
        super(message);
    }
    
    public InvalidLeaveDatesException(String message, Throwable cause) {
        super(message, cause);
    }
} 