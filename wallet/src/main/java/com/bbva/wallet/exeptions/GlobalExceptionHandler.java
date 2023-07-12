package com.bbva.wallet.exeptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Response<String>> handleValidationException(SQLIntegrityConstraintViolationException ex) {
        String field = ex.getMessage();
        Response<String> response = new Response<>();
        response.addError(ErrorCodes.DUPLICATED_VALUE);
        response.setMessage(field);
        response.setData(field);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<Response<String>> handleValidationException(HttpRequestMethodNotSupportedException ex) {
        String field = ex.getMessage();
        String data = ex.getSupportedHttpMethods().toString();

        Response<String> response = new Response<>();
        response.addError(ErrorCodes.WRONG_METHOD);
        response.setMessage(field);
        response.setData(data);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<String>> handleValidationException(ConstraintViolationException ex) {
        String error = ex.getConstraintViolations().iterator().next().getMessage();
        String field = ex.getConstraintViolations().iterator().next().getPropertyPath().toString();
        Response<String> response = new Response<>();
        response.addError(ErrorCodes.INVALID_VALUE);
        response.setMessage(String.format("The field %s isn't valid: [%s]", field, error));
        response.setData(field);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}