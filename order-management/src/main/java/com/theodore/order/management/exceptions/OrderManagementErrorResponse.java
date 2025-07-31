package com.theodore.order.management.exceptions;

import java.time.Instant;

public record OrderManagementErrorResponse(String message, Instant timestamp) {
}
