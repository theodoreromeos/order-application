package com.theodore.order.management.repositories;

import com.theodore.order.management.entities.OrderLine;
import org.springframework.data.repository.CrudRepository;

public interface OrderLineRepository extends CrudRepository<OrderLine, String> {
}
