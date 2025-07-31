package com.theodore.order.management.dtos.responses;

public record OrderLineResponseDto(String productId,
                                   Integer quantity,
                                   Double price) {


}
