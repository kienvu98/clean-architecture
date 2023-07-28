package com.food.ordering.system.restaurant.service.domain.event;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.domain.valueObject.RestaurantId;
import com.food.ordering.system.restaurant.service.domain.entity.OrderApproval;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderRejectEvent extends OrderApprovalEvent{

    private final DomainEventPublisher<OrderRejectEvent> orderRejectEventDomainEventPublisher;

    public OrderRejectEvent(OrderApproval orderApproval, RestaurantId restaurantId, List<String> failureMessage, ZonedDateTime createdAt,
                            DomainEventPublisher<OrderRejectEvent> orderRejectEventDomainEventPublisher) {
        super(orderApproval, restaurantId, failureMessage, createdAt);
        this.orderRejectEventDomainEventPublisher = orderRejectEventDomainEventPublisher;
    }

    @Override
    public void fire() {
        orderRejectEventDomainEventPublisher.publisher(this);
    }
}
