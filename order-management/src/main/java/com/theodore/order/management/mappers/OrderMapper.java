package com.theodore.order.management.mappers;

import com.theodore.order.management.dtos.responses.OrderResponseDto;
import com.theodore.order.management.entities.Orders;
import com.theodore.order.management.entities.enums.OrderStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring", uses = OrderLineMapper.class)
public interface OrderMapper {

    @Mapping(target = "customerName", source = "order.customerName")
    @Mapping(target = "status", source = "order.status", qualifiedByName = "mapStatus")
    @Mapping(target = "orderDate", source = "order.orderDate", qualifiedByName = "mapOrderDate")
    @Mapping(target = "orderLineResponseList", source = "order.orderLines")
    OrderResponseDto mapEntityToResponse(Orders order);

    @Named("mapStatus")
    default String mapStatus(OrderStatus orderStatus) {
        return orderStatus.name();
    }

    @Named("mapOrderDate")
    default LocalDateTime mapOrderDate(Instant date) {
        ZoneId zoneId = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(date, zoneId);
    }

}
