package com.theodore.order.management.repositories;

import com.theodore.order.management.entities.Orders;
import com.theodore.order.management.entities.enums.OrderStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Orders, Long> {

    long deleteByOrderId(Long orderId);

    List<Orders> findDistinctByStatus(OrderStatus status);

}
