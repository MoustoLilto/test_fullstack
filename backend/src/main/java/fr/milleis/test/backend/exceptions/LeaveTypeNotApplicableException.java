package fr.milleis.test.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LeaveTypeNotApplicableException extends RuntimeException {
    
    public LeaveTypeNotApplicableException(String message) {
        super(message);
    }
    
    public LeaveTypeNotApplicableException(String message, Throwable cause) {
        super(message, cause);
    }
} 