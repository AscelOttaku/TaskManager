package kg.com.taskmanager.exceptions.handler;

import jakarta.servlet.http.HttpServletRequest;
import kg.com.taskmanager.exceptions.*;
import kg.com.taskmanager.exceptions.body.ExceptionResponse;
import kg.com.taskmanager.exceptions.body.ValidationExceptionBody;
import kg.com.taskmanager.service.ErrorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class AppExceptionHandler {
    private final ErrorService errorService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ValidationExceptionBody handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request
    ) {
        return errorService.handleValidationException(ex, request);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleAlreadyExistsException(AlreadyExistsException e) {
        return buildResponse(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({BadCredentialsException.class, SecurityException.class})
    public ResponseEntity<ExceptionResponse> handleBadCredentialsException(RuntimeException e) {
        return buildResponse(e, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidPasswordException(InvalidPasswordException e) {
        return buildResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalArgumentException.class, NoSuchElementException.class})
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentOrNoSuchElement(RuntimeException e) {
        if (e instanceof IllegalArgumentException) {
            return buildResponse(e, HttpStatus.CONFLICT);
        } else {
            return buildResponse(e, HttpStatus.NOT_FOUND);
        }
    }

    @ExceptionHandler(HttpClientErrorException.Conflict.class)
    public ResponseEntity<ExceptionResponse> handleHttpClientErrorExceptionConflict(HttpClientErrorException.Conflict e) {
        return buildResponse(e, HttpStatus.CONFLICT);
    }

    private ResponseEntity<ExceptionResponse> buildResponse(RuntimeException e, HttpStatus status) {
        ExceptionResponse response = ExceptionResponse.builder()
                .exceptionClassName(e.getClass().getSimpleName())
                .httpStatus(status)
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(response, status);
    }
}