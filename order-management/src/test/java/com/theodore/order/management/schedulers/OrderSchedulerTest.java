package com.theodore.order.management.schedulers;

import com.theodore.order.management.dtos.responses.LoggingServiceResponse;
import com.theodore.order.management.entities.OrderLine;
import com.theodore.order.management.entities.Orders;
import com.theodore.order.management.entities.enums.OrderStatus;
import com.theodore.order.management.repositories.OrderRepository;
import com.theodore.order.management.services.LoggingServiceRestClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderSchedulerTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private LoggingServiceRestClient loggingServiceRestClient;

    @InjectMocks
    private OrderScheduler orderScheduler;

    @Test
    void shouldProcessUnprocessedOrdersAndSendLogDetails() {
        // given
        Orders order = new Orders();
        order.setOrderId(1L);
        order.setOrderDate(Instant.now());

        OrderLine orderLine1 = new OrderLine();
        orderLine1.setProductId("MARIO KART");
        orderLine1.setQuantity(2);
        orderLine1.setPrice(100.0);
        orderLine1.setOrder(order);

        OrderLine orderLine2 = new OrderLine();
        orderLine2.setProductId("SWITCH 2");
        orderLine2.setQuantity(1);
        orderLine2.setPrice(150.0);
        orderLine2.setOrder(order);

        order.setOrderLines(List.of(orderLine1, orderLine2));

        when(orderRepository.findDistinctByStatus(OrderStatus.UNPROCESSED))
                .thenReturn(List.of(order));
        when(loggingServiceRestClient.sendToLoggingService(any()))
                .thenReturn(new LoggingServiceResponse(true, "all good"));

        // when
        orderScheduler.processOrders();

        // then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PROCESSED);
        verify(orderRepository).saveAll(List.of(order));
        verify(loggingServiceRestClient).sendToLoggingService(any());
    }

    @Test
    void shouldDoNothingWhenNoUnprocessedOrders() {
        // given
        when(orderRepository.findDistinctByStatus(OrderStatus.UNPROCESSED))
                .thenReturn(List.of());

        // when
        orderScheduler.processOrders();

        // then
        verify(orderRepository, never()).saveAll(any());
        verify(loggingServiceRestClient, never()).sendToLoggingService(any());
    }

    @Test
    void shouldLogErrorWhenLoggingServiceFails() {
        // given
        Orders order = new Orders();
        order.setOrderId(2L);
        order.setOrderDate(Instant.now());

        OrderLine line = new OrderLine();
        line.setProductId("SWITCH 2");
        line.setQuantity(3);
        line.setPrice(200.0);
        line.setOrder(order);

        order.setOrderLines(List.of(line));

        when(orderRepository.findDistinctByStatus(OrderStatus.UNPROCESSED)).thenReturn(List.of(order));
        when(loggingServiceRestClient.sendToLoggingService(any()))
                .thenReturn(new LoggingServiceResponse(false, "ERROR"));

        // when
        orderScheduler.processOrders();

        // then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PROCESSED);
        verify(orderRepository).saveAll(List.of(order));
        verify(loggingServiceRestClient).sendToLoggingService(any());
    }

}
