package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderRespone;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.service.domain.dto.track.TrackingOrderResponse;
import com.food.ordering.system.order.service.domain.ports.input.service.OrderApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
class OrderApplicationServiceImpl implements OrderApplicationService {

    private final OrderCreateCommandHandler orderCreateCommandHandler;

    private final OrderTrackingCommandHandler orderTrackingCommandHandler;

    public OrderApplicationServiceImpl(OrderCreateCommandHandler orderCreateCommandHandler,
                                       OrderTrackingCommandHandler orderTrackingCommandHandler) {
        this.orderCreateCommandHandler = orderCreateCommandHandler;
        this.orderTrackingCommandHandler = orderTrackingCommandHandler;
    }

    @Override
    public CreateOrderRespone createOrder(CreateOrderCommand createOrderCommand) {
        CreateOrderRespone createOrderRespone = orderCreateCommandHandler.createOrder(createOrderCommand);
        return createOrderRespone;
    }

    @Override
    public TrackingOrderResponse trackOredr(TrackOrderQuery trackOrderQuery) {
        return orderTrackingCommandHandler.trackingOrder(trackOrderQuery);
    }
}
