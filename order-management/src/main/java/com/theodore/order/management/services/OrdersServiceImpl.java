package com.theodore.order.management.services;

import com.theodore.order.management.dtos.requests.OrderLineRequestDto;
import com.theodore.order.management.dtos.requests.OrderRequestDto;
import com.theodore.order.management.dtos.responses.OrderResponseDto;
import com.theodore.order.management.entities.OrderLine;
import com.theodore.order.management.entities.Orders;
import com.theodore.order.management.exceptions.EmptyOrdersException;
import com.theodore.order.management.exceptions.LogicalValidationException;
import com.theodore.order.management.exceptions.OrderNotFoundException;
import com.theodore.order.management.mappers.OrderLineMapper;
import com.theodore.order.management.mappers.OrderMapper;
import com.theodore.order.management.repositories.OrderLineRepository;
import com.theodore.order.management.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
public class OrdersServiceImpl implements OrdersService {

    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;
    private final OrderLineMapper orderLineMapper;
    private final OrderMapper orderMapper;

    public OrdersServiceImpl(OrderRepository orderRepository,
                             OrderLineRepository orderLineRepository,
                             OrderLineMapper orderLineMapper,
                             OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderLineRepository = orderLineRepository;
        this.orderLineMapper = orderLineMapper;
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional
    public Long createOrder(OrderRequestDto orderDto) {
        if (CollectionUtils.isEmpty(orderDto.orderLines())) {
            throw new EmptyOrdersException();
        }
        var order = new Orders();
        order.setCustomerName(orderDto.customerName());
        Orders savedOrder = orderRepository.save(order);

        var orderLineList = orderDto.orderLines().stream()
                .map(orderLineDto -> {
                    if (orderLineDto.quantity() <= 0) {
                        throw new LogicalValidationException("Quantity of an item cannot be 0 or less.");
                    }
                    return orderLineMapper.mapToEntity(orderLineDto, order);
                })
                .toList();
        orderLineRepository.saveAll(orderLineList);

        return savedOrder.getOrderId();
    }

    @Override
    public OrderResponseDto getOrderById(Long orderId) {
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        return orderMapper.mapEntityToResponse(order);
    }

    @Override
    @Transactional
    public void updateOrder(Long orderId, OrderRequestDto orderRequestDto) {
        if (CollectionUtils.isEmpty(orderRequestDto.orderLines())) {
            throw new EmptyOrdersException();
        }
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        order.setCustomerName(orderRequestDto.customerName());

        List<OrderLine> orderLinesList = order.getOrderLines();

        boolean orderLinesListEmpty = CollectionUtils.isEmpty(orderLinesList);

        for (OrderLineRequestDto orderLineDto : orderRequestDto.orderLines()) {

            if (orderLineDto.quantity() < 0) {
                throw new LogicalValidationException("Quantity of an item cannot be less than zero.");
            }
            // anagkastika O(n^2) edw mallon
            Optional<OrderLine> optionalOrderLine = orderLinesListEmpty ?
                    Optional.empty() : orderLinesList.stream()
                            .filter(orderLine -> orderLine.getProductId().equals(orderLineDto.productId()))
                            .findFirst();

            if (optionalOrderLine.isPresent()) {
                orderLineMapper.updateEntityFromDto(orderLineDto, optionalOrderLine.get());
            } else {
                OrderLine newOrderLine = orderLineMapper.mapToEntity(orderLineDto, order);
                orderLinesList.add(newOrderLine);
            }
        }

        if (!orderLinesListEmpty) {
            orderLinesList.removeIf(orderLine -> orderLine.getQuantity() == 0);
        }
        order.setOrderLines(orderLinesList);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        long deletedItems = orderRepository.deleteByOrderId(orderId);
        if (deletedItems == 0) {
            throw new OrderNotFoundException(orderId);
        }
    }
}
