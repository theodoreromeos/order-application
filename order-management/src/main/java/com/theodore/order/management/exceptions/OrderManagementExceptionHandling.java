package com.theodore.order.management.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class OrderManagementExceptionHandling {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderManagementExceptionHandling.class);

    //todo: add javadoc - handles validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<OrderManagementErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {

        String fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));

        LOGGER.warn("Validation failed [{}]: {}", ex.getBindingResult().getObjectName(), fieldErrors, ex);

        String msg = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList()
                .stream().findFirst().orElse("BAD REQUEST");

        OrderManagementErrorResponse error = new OrderManagementErrorResponse(msg, Instant.now());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyOrdersException.class)
    public ResponseEntity<OrderManagementErrorResponse> handleEmptyOrdersErrors(EmptyOrdersException ex) {
        LOGGER.error("Empty order was placed: {}", ex.getMessage(), ex);
        OrderManagementErrorResponse error = new OrderManagementErrorResponse("Empty order was placed", Instant.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<OrderManagementErrorResponse> handleOrderNotFoundErrors(OrderNotFoundException ex) {
        LOGGER.error("Order was not found error occurred: {}", ex.getMessage(), ex);
        OrderManagementErrorResponse error = new OrderManagementErrorResponse(ex.getMessage(), Instant.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    //TODO : check if runtime has any difference
    @ExceptionHandler(Exception.class)
    public ResponseEntity<OrderManagementErrorResponse> handleGeneral(Exception ex) {
        LOGGER.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        OrderManagementErrorResponse error = new OrderManagementErrorResponse("Unexpected error occurred", Instant.now());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(LogicalValidationException.class)
    public ResponseEntity<OrderManagementErrorResponse> handleLogicalValidationErrors(LogicalValidationException ex) {
        LOGGER.error("Validation failed: {}", ex.getMessage(), ex);
        OrderManagementErrorResponse error = new OrderManagementErrorResponse(ex.getMessage(), Instant.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
