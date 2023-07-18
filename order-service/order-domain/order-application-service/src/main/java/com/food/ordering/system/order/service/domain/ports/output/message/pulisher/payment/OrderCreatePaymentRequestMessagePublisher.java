package com.food.ordering.system.order.service.domain.ports.output.message.pulisher.payment;

import com.food.ordering.system.domain.event.DomainEvent;
import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;

public interface OrderCreatePaymentRequestMessagePublisher extends DomainEventPublisher<OrderCreatedEvent> {
}
