package com.food.ordering.system.restaurant.service.domain.port.output.message.publisher;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.restaurant.service.domain.event.OrderRejectEvent;

public interface OrderRejectMessagePublisher extends DomainEventPublisher<OrderRejectEvent> {
}
