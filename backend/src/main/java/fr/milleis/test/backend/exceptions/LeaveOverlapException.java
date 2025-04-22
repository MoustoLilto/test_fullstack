package fr.milleis.test.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class LeaveOverlapException extends RuntimeException {
    
    public LeaveOverlapException(String message) {
        super(message);
    }
    
    public LeaveOverlapException(String message, Throwable cause) {
        super(message, cause);
    }
} 