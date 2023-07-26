package com.food.ordering.system.payment.service.domain.exception;

import com.food.ordering.system.domain.exception.DomainException;

public class PaymentDomainNotFoundException extends DomainException {
    public PaymentDomainNotFoundException(String message) {
        super(message);
    }

    public PaymentDomainNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
