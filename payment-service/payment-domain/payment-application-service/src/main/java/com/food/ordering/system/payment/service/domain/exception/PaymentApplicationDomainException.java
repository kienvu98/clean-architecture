package com.food.ordering.system.payment.service.domain.exception;

import com.food.ordering.system.domain.exception.DomainException;

public class PaymentApplicationDomainException extends DomainException {

    public PaymentApplicationDomainException(String message) {
        super(message);
    }

    public PaymentApplicationDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
