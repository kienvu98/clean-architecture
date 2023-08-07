package com.food.ordering.system.order.service.dataccess.outbox.payment.exception;

public class PaymentOutboxNotFoundExcetion extends RuntimeException{

    public PaymentOutboxNotFoundExcetion(String message) {
        super(message);
    }
}
