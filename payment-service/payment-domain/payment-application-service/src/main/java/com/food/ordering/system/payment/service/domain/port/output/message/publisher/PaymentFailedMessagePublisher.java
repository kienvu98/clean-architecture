package com.food.ordering.system.payment.service.domain.port.output.message.publisher;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.payment.service.domain.event.PaymentFalledEvent;

public interface PaymentFailedMessagePublisher extends DomainEventPublisher<PaymentFalledEvent> {
}
