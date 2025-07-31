package com.theodore.order.management.schedulers;

import com.theodore.order.management.dtos.requests.SaveLogDetailsRequest;
import com.theodore.order.management.entities.OrderLine;
import com.theodore.order.management.entities.Orders;
import com.theodore.order.management.entities.enums.OrderStatus;
import com.theodore.order.management.repositories.OrderRepository;
import com.theodore.order.management.services.LoggingServiceRestClient;
import jakarta.transaction.Transactional;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
public class OrderScheduler {

    private final static Logger LOGGER = LoggerFactory.getLogger(OrderScheduler.class);

    private final OrderRepository orderRepository;
    private final LoggingServiceRestClient loggingServiceRestClient;

    public OrderScheduler(OrderRepository orderRepository,
                          LoggingServiceRestClient loggingServiceRestClient) {
        this.orderRepository = orderRepository;
        this.loggingServiceRestClient = loggingServiceRestClient;
    }

    @Scheduled(cron = "${scheduler.process.orders.cron}")
    @SchedulerLock(name = "process_orders", lockAtLeastFor = "PT10S", lockAtMostFor = "PT30S")
    @Transactional
    public void processOrders() {//todo: try catch
        LOGGER.info("Processing orders");
        List<Orders> orders = orderRepository.findDistinctByStatus(OrderStatus.UNPROCESSED);

        if (!CollectionUtils.isEmpty(orders)) {
            orders.forEach(order -> order.setStatus(OrderStatus.PROCESSED));

            orderRepository.saveAll(orders);

            List<SaveLogDetailsRequest.LogDetailsDto> logDetailsList = orders.stream()
                    .map(order -> {
                        int itemCount = 0;
                        double amount = 0.0;
                        for (OrderLine orderLine : order.getOrderLines()) {
                            itemCount += orderLine.getQuantity();
                            amount += orderLine.getPrice() * orderLine.getQuantity();
                        }
                        return new SaveLogDetailsRequest.LogDetailsDto(
                                order.getOrderId(),
                                amount,
                                itemCount,
                                mapToLocalDateTime(order.getOrderDate())
                        );
                    })
                    .toList();

            logDetailsList.forEach(logDetail -> System.out.println("order id : "+logDetail.orderId()));

            var loggingServiceResponse = loggingServiceRestClient
                    .sendToLoggingService(new SaveLogDetailsRequest(logDetailsList));
            if (loggingServiceResponse.success()) {
                LOGGER.info("Log details sent successfully to logging service");
            } else {
                LOGGER.error("Error while sending log details to logging service");
            }
        }

    }

    private LocalDateTime mapToLocalDateTime(Instant instant) {
        ZoneId zoneId = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zoneId);
    }

}
