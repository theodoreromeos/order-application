package com.theodore.logging.controllers;

import com.theodore.logging.models.LoggingServiceResponse;
import com.theodore.logging.models.SaveLogDetailsRequest;
import com.theodore.logging.services.LoggingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logs")
public class LoggingController {

    private final LoggingService  loggingService;

    public LoggingController(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    @PostMapping
    public ResponseEntity<LoggingServiceResponse> createOrderEndpoint(@RequestBody @Valid SaveLogDetailsRequest request) {
        var response = loggingService.saveLogDetails(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
