package edu.tomerbu.webfluxsecuritydemo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidJWTException extends RuntimeException {
    public InvalidJWTException() {
        super("Bad JWT");
    }
    public InvalidJWTException(String message) {
        super(message);
    }
    public InvalidJWTException(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidJWTException(Throwable cause) {
        super(cause);
    }
    public InvalidJWTException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}