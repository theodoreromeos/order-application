package com.theodore.order.management.dtos.responses;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDto {

    private String customerName;
    private String status;
    private LocalDateTime orderDate;
    private List<OrderLineResponseDto> orderLineResponseList;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public List<OrderLineResponseDto> getOrderLineResponseList() {
        return orderLineResponseList;
    }

    public void setOrderLineResponseList(List<OrderLineResponseDto> orderLineResponseList) {
        this.orderLineResponseList = orderLineResponseList;
    }

}
