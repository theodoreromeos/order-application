package com.theodore.order.management.utils;

import com.theodore.order.management.dtos.requests.OrderLineRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NoDuplicateProductValidator implements ConstraintValidator<NoDuplicateProduct, List<OrderLineRequestDto>> {

    @Override
    public boolean isValid(List<OrderLineRequestDto> orderLinesList, ConstraintValidatorContext context) {
        if (orderLinesList == null || orderLinesList.isEmpty()) {
            return true;
        }
        Set<String> productSet = new HashSet<>();
        for (OrderLineRequestDto orderLine : orderLinesList) {
            if (!productSet.add(orderLine.productId())) {
                return false;
            }
        }
        return true;
    }
}
