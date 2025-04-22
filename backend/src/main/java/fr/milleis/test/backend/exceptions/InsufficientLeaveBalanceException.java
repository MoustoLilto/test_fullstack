package fr.milleis.test.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientLeaveBalanceException extends RuntimeException {
    
    public InsufficientLeaveBalanceException(String message) {
        super(message);
    }
    
    public InsufficientLeaveBalanceException(String message, Throwable cause) {
        super(message, cause);
    }
} 