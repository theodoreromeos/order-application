package com.theodore.order.management.services;

import com.theodore.order.management.dtos.requests.OrderLineRequestDto;
import com.theodore.order.management.dtos.requests.OrderRequestDto;
import com.theodore.order.management.entities.OrderLine;
import com.theodore.order.management.entities.Orders;
import com.theodore.order.management.exceptions.EmptyOrdersException;
import com.theodore.order.management.exceptions.LogicalValidationException;
import com.theodore.order.management.exceptions.OrderNotFoundException;
import com.theodore.order.management.mappers.OrderLineMapper;
import com.theodore.order.management.repositories.OrderRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private static final String CUSTOMER_NAME = "theodore";
    private static final Long ORDER_ID = 1L;

    @Mock
    private OrderRepository orderRepository;

    @Spy
    private OrderLineMapper orderLineMapper = Mappers.getMapper(OrderLineMapper.class);

    @InjectMocks
    private OrdersServiceImpl orderService;

    @Nested
    class UpdateOrderTestss {

        private static final String UPDATED_PRODUCT = "switch 2";
        private static final String NEW_PRODUCT = "mario kart";

        @Test
        void givenNotExistingId_throwOrderNotFound() {
            // given
            OrderLineRequestDto line = new OrderLineRequestDto(UPDATED_PRODUCT, 1, 100.0);
            OrderRequestDto requestDto = new OrderRequestDto(CUSTOMER_NAME, List.of(line));

            when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> orderService.updateOrder(ORDER_ID, requestDto))
                    .isInstanceOf(OrderNotFoundException.class)
                    .hasMessageContaining("Order with id : " + ORDER_ID + " is not found");
        }

        @Test
        void givenEmptyOrderLines_throwEmptyOrdersException() {
            // given
            OrderRequestDto requestDto = new OrderRequestDto(CUSTOMER_NAME, List.of());

            // when
            assertThatThrownBy(() -> orderService.updateOrder(ORDER_ID, requestDto))
                    .isInstanceOf(EmptyOrdersException.class);
        }

        @Test
        void givenNegativeQuantity_throwExceptionForNegativeQuantity() {
            // given
            OrderLineRequestDto line = new OrderLineRequestDto(UPDATED_PRODUCT, -1, 100.0);
            OrderRequestDto requestDto = new OrderRequestDto(CUSTOMER_NAME, List.of(line));

            Orders order = new Orders();
            order.setOrderId(ORDER_ID);
            order.setOrderLines(new ArrayList<>());

            when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));

            // when
            assertThatThrownBy(() -> orderService.updateOrder(ORDER_ID, requestDto))
                    .isInstanceOf(LogicalValidationException.class)
                    .hasMessageContaining("Quantity of an item cannot be less than zero");
        }

        @Test
        void givenCorrectData_updateOrderSuccessfully() {
            // given
            Orders order = createOrders();

            OrderLine existingOrderLine = createOrderLine(3, 525.0, UPDATED_PRODUCT);

            existingOrderLine.setOrder(order);

            order.setOrderLines(new ArrayList<>(List.of(existingOrderLine)));

            OrderLineRequestDto updatedOrderLineDto = new OrderLineRequestDto(UPDATED_PRODUCT, 5, 510.0);
            OrderLineRequestDto newOrderLineDto = new OrderLineRequestDto(NEW_PRODUCT, 1, 70.6);

            OrderRequestDto requestDto = new OrderRequestDto(CUSTOMER_NAME, List.of(updatedOrderLineDto, newOrderLineDto));

            when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));

            // when
            orderService.updateOrder(ORDER_ID, requestDto);

            // then
            assertThat(order.getCustomerName()).isEqualTo(CUSTOMER_NAME);
            assertThat(order.getOrderLines())
                    .hasSize(2)
                    .extracting(OrderLine::getProductId, OrderLine::getQuantity, OrderLine::getPrice)
                    .containsExactlyInAnyOrder(
                            tuple(UPDATED_PRODUCT, 5, 510.0),
                            tuple(NEW_PRODUCT, 1, 70.6)
                    );
            verify(orderLineMapper, times(1)).updateEntityFromDto(updatedOrderLineDto, existingOrderLine);
            verify(orderLineMapper, times(1)).mapToEntity(newOrderLineDto, order);
            verify(orderRepository, times(1)).save(order);
        }

        @Test
        void givenCorrectDataWithItemRemoval_updateOrderSuccessfully() {
            // given
            Orders order = createOrders();

            OrderLine existingOrderLine = createOrderLine(4, 102.5, UPDATED_PRODUCT);

            existingOrderLine.setOrder(order);

            order.setOrderLines(new ArrayList<>(List.of(existingOrderLine)));

            OrderLineRequestDto updatedOrderLineDto = new OrderLineRequestDto(UPDATED_PRODUCT, 0, 106.5);
            OrderLineRequestDto newOrderLineDto = new OrderLineRequestDto(NEW_PRODUCT, 2, 5.5);

            OrderRequestDto requestDto = new OrderRequestDto(CUSTOMER_NAME, List.of(updatedOrderLineDto, newOrderLineDto));

            when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));

            // when
            orderService.updateOrder(ORDER_ID, requestDto);

            // then
            assertThat(order.getCustomerName()).isEqualTo(CUSTOMER_NAME);
            assertThat(order.getOrderLines())
                    .hasSize(1)
                    .extracting(OrderLine::getProductId, OrderLine::getQuantity, OrderLine::getPrice)
                    .containsExactlyInAnyOrder(
                            tuple(NEW_PRODUCT, 2, 5.5)
                    );
            verify(orderLineMapper, times(1)).updateEntityFromDto(updatedOrderLineDto, existingOrderLine);
            verify(orderLineMapper, times(1)).mapToEntity(newOrderLineDto, order);
            verify(orderRepository, times(1)).save(order);
        }
    }


    private OrderLine createOrderLine(int quantity, double price, String product) {
        OrderLine orderLine = new OrderLine();
        orderLine.setProductId(product);
        orderLine.setQuantity(quantity);
        orderLine.setPrice(price);
        return orderLine;
    }

    private Orders createOrders() {
        var orders = new Orders();
        orders.setCustomerName(CUSTOMER_NAME);
        orders.setOrderId(ORDER_ID);
        return orders;
    }

}
