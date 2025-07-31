package com.theodore.order.management.mappers;

import com.theodore.order.management.dtos.requests.OrderLineRequestDto;
import com.theodore.order.management.dtos.responses.OrderLineResponseDto;
import com.theodore.order.management.entities.OrderLine;
import com.theodore.order.management.entities.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderLineMapper {

    @Mapping(target = "order", source = "order")
    @Mapping(target = "price", source = "orderLineDto.price")
    @Mapping(target = "quantity", source = "orderLineDto.quantity")
    @Mapping(target = "productId", source = "orderLineDto.productId")
    @Mapping(target = "id", ignore = true)
    OrderLine mapToEntity(OrderLineRequestDto orderLineDto, Orders order);

    @Mapping(target = "productId", source = "orderLine.productId")
    @Mapping(target = "quantity", source = "orderLine.quantity")
    @Mapping(target = "price", source = "orderLine.price")
    OrderLineResponseDto mapEntityToResponse(OrderLine orderLine);

    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    void updateEntityFromDto(OrderLineRequestDto dto, @MappingTarget OrderLine entity);

}
