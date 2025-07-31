package com.theodore.logging.services;

import com.theodore.logging.entities.LogDetails;
import com.theodore.logging.models.LoggingServiceResponse;
import com.theodore.logging.models.SaveLogDetailsRequest;
import com.theodore.logging.repositories.LogDetailsRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggingServiceImpl implements LoggingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingServiceImpl.class);

    private final LogDetailsRepository logDetailsRepository;
    private final ModelMapper modelMapper;

    public LoggingServiceImpl(LogDetailsRepository logDetailsRepository,
                              ModelMapper modelMapper) {
        this.logDetailsRepository = logDetailsRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public LoggingServiceResponse saveLogDetails(SaveLogDetailsRequest request) {
        LOGGER.info("Received request to save log details");

        var logDetailsList = request.logDetails().stream()
                .map(ld -> modelMapper.map(ld, LogDetails.class)).toList();

        logDetailsRepository.saveAll(logDetailsList);
        return new LoggingServiceResponse(true, "success");
    }

}
