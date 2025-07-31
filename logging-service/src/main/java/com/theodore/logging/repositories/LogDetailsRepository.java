package com.theodore.logging.repositories;

import com.theodore.logging.entities.LogDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogDetailsRepository  extends MongoRepository<LogDetails, Long> {
}
