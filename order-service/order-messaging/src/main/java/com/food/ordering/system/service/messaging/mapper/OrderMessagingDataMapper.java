package com.food.ordering.system.service.messaging.mapper;

import com.food.ordering.system.domain.valueObject.OrderApprovalStatus;
import com.food.ordering.system.domain.valueObject.PaymentStatus;
import com.food.ordering.system.kafka.order.avro.model.*;
import com.food.ordering.system.order.service.domain.dto.message.CustomerModel;
import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordering.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalEventPayload;
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentEventPayload;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderMessagingDataMapper {

    public PaymentRequestAvroModel orderCreateEventToPaymentRequestAvroModel(OrderCreatedEvent orderCreatedEvent) {
        Order order = orderCreatedEvent.getOrder();
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setCustomerId(order.getCustomerId().getValue().toString())
                .setOrderId(order.getId().getValue().toString())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderCreatedEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.PENDING)
                .build();
    }

    public PaymentRequestAvroModel orderCancelledEventToPaymentRequestAvroModel(OrderCancelledEvent orderCancelledEvent) {
        Order order = orderCancelledEvent.getOrder();
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setCustomerId(order.getCustomerId().getValue().toString())
                .setOrderId(order.getId().getValue().toString())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderCancelledEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.CANCELLED)
                .build();
    }

    public RestaurantApprovalRequestAvroModel orderPaiEventToRestaurantApprovalRequestAvroModel(OrderPaidEvent orderPaidEvent) {
        Order order = orderPaidEvent.getOrder();
        return RestaurantApprovalRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setOrderId(order.getId().getValue().toString())
                .setRestaurantId(order.getRestaurantId().getValue().toString())
                .setRestaurantOrderStatus(RestaurantOrderStatus.valueOf(order.getOrderStatus().name()))
                .setProducts(order.getItems().stream().map(orderItem ->
                                Product.newBuilder()
                                        .setId(orderItem.getProduct().getId().getValue().toString())
                                        .setQuantity(orderItem.getQuantity())
                                        .build()).collect(Collectors.toList()))
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderPaidEvent.getCreatedAt().toInstant())
                .setRestaurantOrderStatus(RestaurantOrderStatus.PAID)
                .build();
    }

    public PaymentResponse paymentResponseAvroModelToPaymentResponse(PaymentResponseAvroModel paymentResponseAvroModel) {
        return PaymentResponse.builder()
                .id(paymentResponseAvroModel.getId())
                .orderId(paymentResponseAvroModel.getOrderId())
                .sagaId(paymentResponseAvroModel.getSagaId())
                .customerId(paymentResponseAvroModel.getCustomerId())
                .paymentId(paymentResponseAvroModel.getPaymentId())
                .price(paymentResponseAvroModel.getPrice())
                .createdAt(paymentResponseAvroModel.getCreatedAt())
                .paymentStatus(PaymentStatus.valueOf(paymentResponseAvroModel.getPaymentStatus().name()))
                .failureMessage(paymentResponseAvroModel.getFailureMessages())
                .build();
    }

    public RestaurantApprovalResponse restaurantApprovalResponseAvroModelToRestaurantApprovalResponse
            (RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel) {
        return RestaurantApprovalResponse.builder()
                .id(restaurantApprovalResponseAvroModel.getId())
                .restaurantId(restaurantApprovalResponseAvroModel.getRestaurantId())
                .orderId(restaurantApprovalResponseAvroModel.getOrderId())
                .sagaId(restaurantApprovalResponseAvroModel.getSagaId())
                .createdAt(restaurantApprovalResponseAvroModel.getCreatedAt())
                .orderApprovalStatus(OrderApprovalStatus.valueOf(restaurantApprovalResponseAvroModel.getOrderApprovalStatus().name()))
                .failureMessage(restaurantApprovalResponseAvroModel.getFailureMessages())
                .build();
    }

    public PaymentRequestAvroModel orderPaymentEventToPaymentRequestAvroModel(String sagaId, OrderPaymentEventPayload orderPaymentEventPayload) {
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId(sagaId)
                .setCustomerId(orderPaymentEventPayload.getCustomerId())
                .setOrderId(orderPaymentEventPayload.getOrderId())
                .setPrice(orderPaymentEventPayload.getPrice())
                .setCreatedAt(orderPaymentEventPayload.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.valueOf(orderPaymentEventPayload.getPaymentOrderStatus()))
                .build();
    }

    public RestaurantApprovalRequestAvroModel orderApprovalEventToRestaurantApprovalRequestAvroModel(String sagaId, OrderApprovalEventPayload orderApprovalEventPayload) {
        return RestaurantApprovalRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId(sagaId)
                .setOrderId(orderApprovalEventPayload.getOrderId())
                .setRestaurantId(orderApprovalEventPayload.getRestaurantId())
                .setRestaurantOrderStatus(RestaurantOrderStatus.valueOf(orderApprovalEventPayload.getRestaurantOrderStatus()))
                .setProducts(orderApprovalEventPayload.getProducts().stream().map(orderApprovalEventProduct ->
                        Product.newBuilder()
                                .setId(orderApprovalEventProduct.getId())
                                .setQuantity(orderApprovalEventProduct.getQuantity())
                                .build()).collect(Collectors.toList()))
                .build();
    }

    public CustomerModel customerAvroModelToCustomerModel(CustomerAvroModel customerAvroModel) {
        return CustomerModel.builder()
                .id(customerAvroModel.getId())
                .firstName(customerAvroModel.getFirstName())
                .userName(customerAvroModel.getUsername())
                .lastName(customerAvroModel.getLastName())
                .build();
    }
}
