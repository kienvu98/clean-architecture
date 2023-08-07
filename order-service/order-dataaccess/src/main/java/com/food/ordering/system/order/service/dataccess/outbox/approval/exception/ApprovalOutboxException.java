package com.food.ordering.system.order.service.dataccess.outbox.approval.exception;

public class ApprovalOutboxException extends RuntimeException{

    public ApprovalOutboxException(String message) {
        super(message);
    }
}
