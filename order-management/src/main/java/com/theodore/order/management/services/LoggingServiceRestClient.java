package com.theodore.order.management.services;

import com.theodore.order.management.dtos.requests.SaveLogDetailsRequest;
import com.theodore.order.management.dtos.responses.LoggingServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class LoggingServiceRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingServiceRestClient.class);

    private final RestClient restClient;

    public LoggingServiceRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public LoggingServiceResponse sendToLoggingService(SaveLogDetailsRequest saveLogDetailsRequest) {
        LOGGER.info("Sending orders to logging service");
        return restClient.post()
                .uri("/logs")
                .body(saveLogDetailsRequest)
                .retrieve()
                .body(LoggingServiceResponse.class);
    }

}
