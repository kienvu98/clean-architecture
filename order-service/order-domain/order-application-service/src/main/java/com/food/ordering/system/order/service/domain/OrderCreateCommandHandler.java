package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderRespone;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.output.message.pulisher.payment.OrderCreatePaymentRequestMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class OrderCreateCommandHandler {

    private final OrderDataMapper orderDataMapper;

    private final OrderCreateHelper orderCreateHelper;

    private final OrderCreatePaymentRequestMessagePublisher orderCreatePaymentRequestMessagePublisher;

    public OrderCreateCommandHandler(OrderDataMapper orderDataMapper,
                                     OrderCreateHelper orderCreateHelper,
                                     OrderCreatePaymentRequestMessagePublisher orderCreatePaymentRequestMessagePublisher) {
        this.orderDataMapper = orderDataMapper;
        this.orderCreateHelper = orderCreateHelper;
        this.orderCreatePaymentRequestMessagePublisher = orderCreatePaymentRequestMessagePublisher;
    }

    @Transactional
    public CreateOrderRespone createOrder(CreateOrderCommand createOrderCommand) {
        OrderCreatedEvent createdEvent = orderCreateHelper.persistOrder(createOrderCommand);
        log.info("Order is created with id: {}", createdEvent.getOrder().getId().getValue());
        orderCreatePaymentRequestMessagePublisher.publisher(createdEvent);
        return orderDataMapper.orderToCreateOrderResponse(createdEvent.getOrder());
    }
}
