package com.food.ordering.system.restaurant.service.domain.port.input.message.listener;

import com.food.ordering.system.restaurant.service.domain.dto.RestaurantApprovalRequest;

public interface RestaurantApprovalRequestMessageListener {

    void approvedOrder(RestaurantApprovalRequest restaurantApprovalRequest);
}
