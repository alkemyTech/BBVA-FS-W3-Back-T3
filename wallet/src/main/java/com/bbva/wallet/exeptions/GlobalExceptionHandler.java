package com.bbva.wallet.exeptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
@RestController
public class GlobalExceptionHandler {

    private ResponseEntity<Response<String>> getResponseResponseEntity(String error, String field, ErrorCodes errorCode, HttpStatus httpStatus) {
        Response<String> response = new Response<>();
        response.addError(errorCode);
        response.setMessage(String.format("The field %s isn't valid: [%s]", field, error));
        response.setData(field);
        return ResponseEntity.status(httpStatus).body(response);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Response<String>> handleValidationException(SQLIntegrityConstraintViolationException ex) {
        String field = ex.getMessage();
        return getResponseResponseEntity(field, field, ErrorCodes.DUPLICATED_VALUE, HttpStatus.CONFLICT);
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
        return getResponseResponseEntity(error, field, ErrorCodes.INVALID_VALUE, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<String>> handleValidationException(MethodArgumentNotValidException ex) {
        String error = String.valueOf(ex.getBody());
        String field = ex.getParameter().getParameterName();
        return getResponseResponseEntity(error, field, ErrorCodes.INVALID_VALUE, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Response<String>> handleValidationException(BaseException ex) {
        Response<String> response = new Response<>();
        response.addError(ex.getErrorCodes());
        response.setMessage(ex.getMessage());
        response.setData("");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

}