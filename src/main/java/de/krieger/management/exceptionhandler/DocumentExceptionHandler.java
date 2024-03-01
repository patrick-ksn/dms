package de.krieger.management.exceptionhandler;
import de.krieger.management.exception.DocumentNotFoundException;
import de.krieger.management.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
/**
 * Global exception handler for handling DocumentNotFoundException.
 */
@ControllerAdvice
@RestController
@SuppressWarnings("unused")
public class DocumentExceptionHandler {
    /**
     * Handles DocumentNotFoundException and returns an error response with HTTP status 404.
     *
     * @param documentNotFoundException The DocumentNotFoundException to handle.
     * @return ResponseEntity containing the error response and HTTP status 404.
     */
    @ExceptionHandler(DocumentNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleResourceNotFoundException(DocumentNotFoundException documentNotFoundException) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), documentNotFoundException.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
