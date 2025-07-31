package com.theodore.order.management.exceptions;

public class EmptyOrdersException extends RuntimeException {

    public EmptyOrdersException() {
        super("Orders cannot be empty");
    }

}
