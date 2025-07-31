package com.theodore.order.management.dtos.requests;

import java.time.LocalDateTime;
import java.util.List;

public record SaveLogDetailsRequest(List<LogDetailsDto> logDetails) {

    public record LogDetailsDto(
            Long orderId,
            double amount,
            int itemsCount,
            LocalDateTime dateTime
    ) {
    }

}
