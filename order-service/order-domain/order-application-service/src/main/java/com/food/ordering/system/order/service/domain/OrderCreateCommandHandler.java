package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderRespone;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentEventPayload;
import com.food.ordering.system.order.service.domain.outbox.scheduler.payment.PaymentOutboxHelper;
import com.food.ordering.system.outbox.OutboxStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
public class OrderCreateCommandHandler {

    private final OrderDataMapper orderDataMapper;
    private final OrderCreateHelper orderCreateHelper;
    private final PaymentOutboxHelper paymentOutboxHelper;
    private final OrderSagaHelper orderSagaHelper;


    public OrderCreateCommandHandler(OrderDataMapper orderDataMapper,
                                     OrderCreateHelper orderCreateHelper,
                                     PaymentOutboxHelper paymentOutboxHelper,
                                     OrderSagaHelper orderSagaHelper) {
        this.orderDataMapper = orderDataMapper;
        this.orderCreateHelper = orderCreateHelper;
        this.paymentOutboxHelper = paymentOutboxHelper;
        this.orderSagaHelper = orderSagaHelper;
    }

    @Transactional
    public CreateOrderRespone createOrder(CreateOrderCommand createOrderCommand) {
        OrderCreatedEvent createdEvent = orderCreateHelper.persistOrder(createOrderCommand);
        log.info("Order is created with id: {}", createdEvent.getOrder().getId().getValue());
        CreateOrderRespone createOrderRespone =
                orderDataMapper.orderToCreateOrderResponse(createdEvent.getOrder(), "Order Created Successfuly");
        OrderPaymentEventPayload orderPaymentEventPayload = orderDataMapper.orderCreatedEvenToOrderPaymentEventPayload(createdEvent);
        paymentOutboxHelper.savePaymentOutboxMessage(orderPaymentEventPayload, createdEvent.getOrder().getOrderStatus(),
                orderSagaHelper.orderSatatusToSagaStatus(createdEvent.getOrder().getOrderStatus()), OutboxStatus.STARTED, UUID.randomUUID());
        log.info("Returing CreateOrderResponse with order id: {}", createdEvent.getOrder().getId());
        return createOrderRespone;
    }
}
