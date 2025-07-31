package com.theodore.order.management.dtos.requests;

import com.theodore.order.management.utils.NoDuplicateProduct;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record OrderRequestDto(@NotBlank String customerName,
                              @Valid @NoDuplicateProduct List<OrderLineRequestDto> orderLines) {
}
