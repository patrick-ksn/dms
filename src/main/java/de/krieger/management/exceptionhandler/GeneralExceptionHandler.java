package de.krieger.management.exceptionhandler;
import de.krieger.management.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
/**
 * Global exception handler for handling general exceptions and validation errors.
 */
@ControllerAdvice
@RestController
@SuppressWarnings("unused")
public class GeneralExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GeneralExceptionHandler.class);
    /**
     * Handles all exceptions and returns an error response with HTTP status 400 (Bad Request).
     *
     * @param ex The exception to handle.
     * @return ResponseEntity containing the error response and HTTP status 400.
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        logger.error("Exception cause by: " , ex);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    /**
     * Handles MethodArgumentNotValidException and returns an error response with HTTP status 400 (Bad Request).
     *
     * @param methodArgumentNotValidException The MethodArgumentNotValidException to handle.
     * @return ErrorResponse containing the validation error messages.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException methodArgumentNotValidException) {
        List<String> errors = methodArgumentNotValidException.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        String errorMessage = String.join(", ", errors);
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage);
    }

}
