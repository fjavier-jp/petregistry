package org.hopto.fjavierjp.petregistry.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest request)
    {
        ErrorDetails errorDetails = new ErrorDetails(
            new Date(),
            exception.getMessage(),
            request.getDescription(false),
            exception.getStackTrace().toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<?> handleStorageException(StorageException exception, WebRequest request)
    {
        ErrorDetails errorDetails = new ErrorDetails(
            new Date(),
            exception.getMessage(),
            request.getDescription(false),
            exception.getStackTrace().toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
