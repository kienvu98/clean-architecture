package com.food.ordering.system.order.service.application.rest;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderRespone;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.service.domain.dto.track.TrackingOrderResponse;
import com.food.ordering.system.order.service.domain.ports.input.service.OrderApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/orders", produces = "application/vnd.api.v1+json")
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    public OrderController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @PostMapping
    public ResponseEntity<CreateOrderRespone> createOrder(@RequestBody CreateOrderCommand createOrderCommand) {
        log.info("Creating order for customer: {} at restaurant: {}", createOrderCommand.getCustomerId(), createOrderCommand.getRestauranId());
        CreateOrderRespone createOrderRespone = orderApplicationService.createOrder(createOrderCommand);
        log.info("Order created with tracking id:", createOrderRespone.getOrderTrackingId());
        return ResponseEntity.ok(createOrderRespone);
    }

    @GetMapping("/{trackingId}")
    public ResponseEntity<TrackingOrderResponse> getOrderbyTrackingId(@PathVariable  UUID trackingId){
        TrackingOrderResponse trackingOrderResponse =
                orderApplicationService.trackOredr(TrackOrderQuery.builder().orderTrackingId(trackingId).build());
        log.info("Returnibg order status with tracking id: {}", trackingOrderResponse.getOrderTrackingId());
        return ResponseEntity.ok(trackingOrderResponse);
    }
}
