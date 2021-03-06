package nl.berwout.api.services;

import nl.berwout.api.exceptions.InvalidDateException;
import nl.berwout.api.exceptions.InvalidFileFormatException;
import nl.berwout.api.exceptions.InvalidInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * used to capture our custom exceptions and display return the error messages.
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({InvalidFileFormatException.class, InvalidInputException.class, InvalidDateException.class})
    protected ResponseEntity<String> handleInvalidInput(Exception ex, WebRequest request) {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
