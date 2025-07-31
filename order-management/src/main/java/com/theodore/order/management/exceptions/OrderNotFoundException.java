package com.theodore.order.management.exceptions;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(Long id) {
        super("Order with id : " + id + " is not found");
    }

}
