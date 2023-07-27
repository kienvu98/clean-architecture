package com.food.ordering.system.payment.service.domain.event;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.payment.service.domain.entity.Payment;

import java.time.ZonedDateTime;
import java.util.Collections;

public class PaymentCompleteEvent extends PaymentEvent{

    private final DomainEventPublisher<PaymentCompleteEvent> paymentCompleteEventDomainEventPublisher;

    public PaymentCompleteEvent(Payment payment, ZonedDateTime createdAt,
                                DomainEventPublisher<PaymentCompleteEvent> paymentCompleteEventDomainEventPublisher) {
        super(payment, createdAt, Collections.emptyList());
        this.paymentCompleteEventDomainEventPublisher = paymentCompleteEventDomainEventPublisher;
    }

    @Override
    public void fire() {
        paymentCompleteEventDomainEventPublisher.publisher(this);
    }
}
