package com.theodore.logging.exceptions;

import com.theodore.logging.models.LoggingServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LoggingServiceExceptionHandling {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingServiceExceptionHandling.class);

    @ExceptionHandler(Exception.class)
    public LoggingServiceResponse handleExceptions(Exception ex) {
        LOGGER.error("Error occurred: {}", ex.getMessage(), ex);
        return new LoggingServiceResponse(false, ex.getMessage());
    }

}
