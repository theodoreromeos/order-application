package com.theodore.order.management.services;

import com.theodore.order.management.dtos.requests.OrderRequestDto;
import com.theodore.order.management.dtos.responses.OrderResponseDto;

public interface OrdersService {

    /**
     * This method validates the incoming order data, creates a new order,
     * maps and saves each order line, and returns the id of the created order.
     *
     * @param orderDto an {@link OrderRequestDto} object containing the customer's name and a list of order lines
     * @return {@link Long} representing the database ID of the newly created order entity
     * @throws com.theodore.order.management.exceptions.EmptyOrdersException       if the orderDto does not contain any order lines
     * @throws com.theodore.order.management.exceptions.LogicalValidationException if any order line has a quantity less than or equal to 0
     */
    Long createOrder(OrderRequestDto orderDto);

    /**
     * Retrieves an existing order by id from the database, maps it into a DTO and returns it
     *
     * @param orderId a {@link Long} representing the unique identifier of the order to retrieve
     * @return {@link OrderResponseDto} containing customer name and associated order line information
     * @throws com.theodore.order.management.exceptions.OrderNotFoundException if no order with the given ID exists in the database
     */
    OrderResponseDto getOrderById(Long orderId);

    /**
     * Updates an existing order with new customer name and order line data.
     * This method validates incoming data, updates existing lines, adds new ones,
     * and removes any lines with zero quantity
     *
     * @param orderId is a {@link Long} representing the ID of the order to be updated; must not be {@code null}
     * @param orderDto is a {@link OrderRequestDto} containing the updated customer name and list of order lines
     * @throws com.theodore.order.management.exceptions.OrderNotFoundException if the order with the specified ID does not exist
     * @throws com.theodore.order.management.exceptions.EmptyOrdersException if the updated order does not contain any order lines
     * @throws com.theodore.order.management.exceptions.LogicalValidationException if any order line has a negative quantity
     */
    void updateOrder(Long orderId, OrderRequestDto orderDto);

    /**
     * Deletes an order and all its associated order lines from the database.
     *
     * @param orderId is a {@link Long} representing the unique identifier of the order to delete
     * @throws com.theodore.order.management.exceptions.OrderNotFoundException if no order with the given ID exists in the database
     */
    void deleteOrder(Long orderId);

}
