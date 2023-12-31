package com.rmf.demoparkapi.web.exceptions;

import com.rmf.demoparkapi.exceptions.CodUniqueViolationException;
import com.rmf.demoparkapi.exceptions.EntityNotFoundException;
import com.rmf.demoparkapi.exceptions.NumberIdentificationUniqueException;
import com.rmf.demoparkapi.exceptions.UsernameUniqueViolationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request, BindingResult result) {

        log.error("Api Error - ", ex);
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, "Invalid field!!!", result));
    }

    @ExceptionHandler({UsernameUniqueViolationException.class, NumberIdentificationUniqueException.class, CodUniqueViolationException.class})
    public ResponseEntity usernameUniqueViolationException(RuntimeException ex, HttpServletRequest request) {

        log.error("Api Error - ", ex);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.CONFLICT, ex.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity entityNotFoundException(RuntimeException ex, HttpServletRequest request) {

        log.error("Api Error - ", ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity AccessDeniedException(RuntimeException ex, HttpServletRequest request) {

        log.error("Api Error - ", ex);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.FORBIDDEN, ex.getMessage()));
    }
}
