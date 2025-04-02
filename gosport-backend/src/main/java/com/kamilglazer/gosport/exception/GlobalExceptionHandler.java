package com.kamilglazer.gosport.exception;



import com.kamilglazer.gosport.exception.errorEntity.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class, UserWithThisEmailAlreadyExists.class})
    public ResponseEntity<ErrorDetails> handleAllExceptions(Exception ex, WebRequest request) {
        return createErrorResponse(ex, request);
    }

    private ResponseEntity<ErrorDetails> createErrorResponse(Exception ex, WebRequest request) {
        ErrorDetails details = ErrorDetails.builder()
                .error(ex.getMessage())
                .details(request.getDescription(false))
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
    }

}
