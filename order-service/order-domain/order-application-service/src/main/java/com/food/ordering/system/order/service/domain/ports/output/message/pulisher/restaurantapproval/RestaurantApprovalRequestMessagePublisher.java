package com.food.ordering.system.order.service.domain.ports.output.message.pulisher.restaurantapproval;

import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.ordering.system.outbox.OutboxStatus;

import java.util.function.BiConsumer;

public interface RestaurantApprovalRequestMessagePublisher {

    void publish(OrderApprovalOutboxMessage orderApprovalOutboxMessage, BiConsumer<OrderPaymentOutboxMessage, OutboxStatus> outboxCallback);
}