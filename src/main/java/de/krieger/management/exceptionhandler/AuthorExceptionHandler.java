package de.krieger.management.exceptionhandler;

import de.krieger.management.exception.AuthorNotFoundException;
import de.krieger.management.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ExceptionHandler;
/**
 * Global exception handler for handling AuthorNotFoundException.
 */
@ControllerAdvice
@RestController
@SuppressWarnings("unused")
public class AuthorExceptionHandler {
    /**
     * Handles AuthorNotFoundException and returns an error response with HTTP status 404.
     *
     * @param authorNotFoundException The AuthorNotFoundException to handle.
     * @return ResponseEntity containing the error response and HTTP status 404.
     */
    @ExceptionHandler(AuthorNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleResourceNotFoundException(AuthorNotFoundException authorNotFoundException) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), authorNotFoundException.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
