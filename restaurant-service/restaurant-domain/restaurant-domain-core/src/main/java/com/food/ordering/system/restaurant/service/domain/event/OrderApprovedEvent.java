package com.food.ordering.system.restaurant.service.domain.event;

import com.food.ordering.system.domain.valueObject.RestaurantId;
import com.food.ordering.system.restaurant.service.domain.entity.OrderApproval;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderApprovedEvent extends OrderApprovalEvent{

    public OrderApprovedEvent(OrderApproval orderApproval, RestaurantId restaurantId, List<String> failureMessage, ZonedDateTime createdAt) {
        super(orderApproval, restaurantId, failureMessage, createdAt);
    }

}
