package com.food.ordering.system.order.service.domain.ports.input.service;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderRespone;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.service.domain.dto.track.TrackingOrderResponse;

import javax.validation.Valid;

public interface OrderApplicationService {

    CreateOrderRespone createOrder(@Valid  CreateOrderCommand createOrderCommand);

    TrackingOrderResponse trackOredr(@Valid  TrackOrderQuery trackOrderQuery);
}
