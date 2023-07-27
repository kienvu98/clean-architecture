package com.food.ordering.system.payment.service.domain.event;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.payment.service.domain.entity.Payment;

import java.time.ZonedDateTime;
import java.util.List;

public class PaymentFalledEvent extends PaymentEvent{

    private final DomainEventPublisher<PaymentFalledEvent> paymentFalledEventDomainEventPublisher;

    public PaymentFalledEvent(Payment payment, ZonedDateTime createdAt, List<String> failureMessage,
                              DomainEventPublisher<PaymentFalledEvent> paymentFalledEventDomainEventPublisher) {
        super(payment, createdAt, failureMessage);
        this.paymentFalledEventDomainEventPublisher = paymentFalledEventDomainEventPublisher;
    }

    @Override
    public void fire() {
        paymentFalledEventDomainEventPublisher.publisher(this);
    }
}
