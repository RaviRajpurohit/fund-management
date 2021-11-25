package com.sapient.ws;

import com.sapient.module.exception.InvalidResourceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Object> handleInvalidArgument(RuntimeException ex, WebRequest request) {

        String message = "Invalid required parameter. please recheck.";

        return new ResponseEntity<>(message, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidResourceException.class})
    public ResponseEntity<Object> handleInvalidResource(RuntimeException ex, WebRequest request) {

        String message = "Invalid request. does not found any resource of given information. please recheck.";

        return new ResponseEntity<>(message, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
