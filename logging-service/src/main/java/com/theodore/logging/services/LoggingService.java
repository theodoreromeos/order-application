package com.theodore.logging.services;

import com.theodore.logging.models.LoggingServiceResponse;
import com.theodore.logging.models.SaveLogDetailsRequest;

public interface LoggingService {

    /**
     * Saves a list of log details to the database.
     * This method maps each {@link com.theodore.logging.models.SaveLogDetailsRequest.LogDetailsDto}
     * from the incoming request into a {@link com.theodore.logging.entities.LogDetails}
     * entity, saves them to a mongodb, and returns a success response.
     *
     * @param request is a {@link SaveLogDetailsRequest} containing a list of log detail DTOs
     * @return a {@link LoggingServiceResponse} indicating success status and a response message
     */
    LoggingServiceResponse saveLogDetails(SaveLogDetailsRequest request);

}
