package com.theodore.order.management.controllers;

import com.theodore.order.management.dtos.requests.OrderRequestDto;
import com.theodore.order.management.dtos.responses.OrderResponseDto;
import com.theodore.order.management.services.OrdersService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @PostMapping
    @Operation(summary = "Creates a new order")
    public ResponseEntity<Long> createOrderEndpoint(@RequestBody @Valid OrderRequestDto orderDto) {
        Long id = ordersService.createOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

//    @PostMapping
//    public ResponseEntity<Void> createOrderEndpoint(@RequestBody @Valid CreateNewOrderRequestDto orderDto,
//                                                    UriComponentsBuilder uriComponentsBuilder) {
//
//        Long id = ordersService.createOrder(orderDto);
//
//        UriComponents uriComponents = uriComponentsBuilder.path("/orders/{id}").buildAndExpand(id);
//        return ResponseEntity.created(uriComponents.toUri()).build();
//    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an order by id")
    public ResponseEntity<OrderResponseDto> getOrderByIdEndpoint(@PathVariable Long id) {
        var orderResponse = ordersService.getOrderById(id);
        return ResponseEntity.status(HttpStatus.OK).body(orderResponse);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates an order by id")
    public ResponseEntity<Void> updateOrderByIdEndpoint(@PathVariable Long id,
                                                        @RequestBody @Valid OrderRequestDto orderDto) {
        ordersService.updateOrder(id, orderDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes an order by id")
    public ResponseEntity<Void> deleteOrderByIdEndpoint(@PathVariable Long id) {
        ordersService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }

}
