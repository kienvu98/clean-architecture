package com.food.ordering.system.restaurant.service.domain;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.domain.valueObject.OrderApprovalStatus;
import com.food.ordering.system.restaurant.service.domain.entity.Restaurant;
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovalEvent;
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovedEvent;
import com.food.ordering.system.restaurant.service.domain.event.OrderRejectEvent;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static com.food.ordering.system.domain.DomainConstants.UTC;

@Slf4j
public class RestaurantDomainServiceImpl implements RestaurantDomainService{

    @Override
    public OrderApprovalEvent valiadateOrder(Restaurant restaurant, List<String> failureMessage) {
        restaurant.validateOrder(failureMessage);
        log.info("Validating order with id: {}", restaurant.getOrderDetail().getId().getValue());
        if (failureMessage.isEmpty()) {
            log.info("Order is approval for order id: {}", restaurant.getOrderDetail().getId().getValue());
            restaurant.constructOrderApproval(OrderApprovalStatus.APPROVED);
            return new OrderApprovedEvent(restaurant.getOrderApproval(), restaurant.getId(), failureMessage,
                    ZonedDateTime.now(ZoneId.of(UTC)));
        } else {
            log.info("Order is reject for order id: {}", restaurant.getOrderDetail().getId().getValue());
            restaurant.constructOrderApproval(OrderApprovalStatus.REJECTED);
            return new OrderRejectEvent(restaurant.getOrderApproval(), restaurant.getId(), failureMessage,
                    ZonedDateTime.now(ZoneId.of(UTC)));
        }
    }
}
