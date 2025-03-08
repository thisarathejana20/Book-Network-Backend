package edu.icet.book.book_network.util.exception;

import edu.icet.book.book_network.dto.ExceptionResponse;
import edu.icet.book.book_network.util.BusinessErrorCodes;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

import static edu.icet.book.book_network.util.BusinessErrorCodes.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handleException(LockedException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(ACCOUNT_LOCKED.getCode())
                        .errorDescription(ACCOUNT_LOCKED.getDescription())
                        .error(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handleException(DisabledException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(ACCOUNT_DISABLED.getCode())
                        .errorDescription(ACCOUNT_DISABLED.getDescription())
                        .error(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleException(BadCredentialsException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BAD_CREDENTIALS.getCode())
                        .errorDescription(BAD_CREDENTIALS.getDescription())
                        .error(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse> handleException(MessagingException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.builder()
                        .error(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException exception) {
        Set<String> errors = new HashSet<>();
        exception.getBindingResult()
                .getAllErrors()
                .forEach(objectError -> {
                    var errorMessage = objectError.getDefaultMessage();
                    errors.add(errorMessage);
                });
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionResponse.builder()
                        .validationErrors(errors)
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exception) {
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.builder()
                        .errorDescription("Internal server error. Please try again later or contact support.")
                        .error(exception.getMessage())
                        .build());
    }
}
