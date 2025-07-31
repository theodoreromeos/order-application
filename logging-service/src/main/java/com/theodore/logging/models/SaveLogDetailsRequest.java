package com.theodore.logging.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.List;

public record SaveLogDetailsRequest(List<LogDetailsDto> logDetails) {

    //did it with record at first and model mapper could not map it
    public static class LogDetailsDto {

        @NotNull
        @Positive
        private Long orderId;

        @NotNull
        @Positive
        private double amount;

        @NotNull
        @Positive
        private int itemsCount;

        @NotNull
        private LocalDateTime dateTime;

        public Long getOrderId() {
            return orderId;
        }

        public void setOrderId(Long orderId) {
            this.orderId = orderId;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public int getItemsCount() {
            return itemsCount;
        }

        public void setItemsCount(int itemsCount) {
            this.itemsCount = itemsCount;
        }

        public LocalDateTime getDateTime() {
            return dateTime;
        }

        public void setDateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
        }
    }

}
