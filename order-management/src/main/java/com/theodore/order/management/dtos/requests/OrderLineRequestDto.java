package com.theodore.order.management.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderLineRequestDto(@NotBlank String productId,
                                  @NotNull Integer quantity,
                                  @NotNull @Positive Double price) {
}
