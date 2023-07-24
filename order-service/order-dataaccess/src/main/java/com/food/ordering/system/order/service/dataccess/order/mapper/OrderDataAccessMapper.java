package com.food.ordering.system.order.service.dataccess.order.mapper;

import com.food.ordering.system.domain.valueObject.*;
import com.food.ordering.system.order.service.dataccess.order.entity.OrderAddressEntity;
import com.food.ordering.system.order.service.dataccess.order.entity.OrderEntity;
import com.food.ordering.system.order.service.dataccess.order.entity.OrderItemEntity;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.valueobject.OrderItemId;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.food.ordering.system.order.service.domain.entity.Order.FAILURE_MESSAGE_DELIMITER;

@Component
public class OrderDataAccessMapper {

    public OrderEntity orderToOrderEntity(Order order) {
        OrderEntity orderEntity = OrderEntity.builder()
                .id(order.getId().getValue())
                .customerId(order.getCustomerId().getValue())
                .restaurantId(order.getRestaurantId().getValue())
                .trackingId(order.getTrackingId().getValue())
                .address(deliveryAddressToAddressEntity(order.getDeliveryAddress()))
                .items(orderItemsToOrderItemsEntities(order.getItems()))
                .orderStatus(order.getOrderStatus())
                .failureMessage(order.getFailureMessages() != null ?
                        String.join(FAILURE_MESSAGE_DELIMITER, order.getFailureMessages()) : "")
                .build();

        orderEntity.getAddress().setOrder(orderEntity);
        orderEntity.getItems().forEach(item -> item.setOrder(orderEntity));
        return orderEntity;
    }

    public Order orderEntityToOrder(OrderEntity orderEntity) {
        return Order.builder()
                .orderId(new OrderId((orderEntity.getId())))
                .customerId(new CustomerId(orderEntity.getCustomerId()))
                .restaurantId(new RestaurantId(orderEntity.getRestaurantId()))
                .trackingId(new TrackingId(orderEntity.getTrackingId()))
                .deliveryAddress(addressEntityToDeleveryAddress(orderEntity.getAddress()))
                .price(new Money(orderEntity.getPrice()))
                .items(orderItemEntitiesToOrderItems(orderEntity.getItems()))
                .orderStatus(orderEntity.getOrderStatus())
                .failureMessages(orderEntity.getFailureMessage().isEmpty() ? new ArrayList<>() :
                       new ArrayList<>(Arrays.asList(orderEntity.getFailureMessage().split(FAILURE_MESSAGE_DELIMITER))))
                .build();
    }

    private List<OrderItem> orderItemEntitiesToOrderItems(List<OrderItemEntity> items) {
        return items.stream().map(item -> OrderItem.builder()
                .orderItemId(new OrderItemId(item.getId()))
                .product(new Product(new ProductId(item.getProductId())))
                .price(new Money(item.getPrice()))
                .quantity(item.getQuantity())
                .subTotal(new Money(item.getSubTotal()))
                .build())
                .collect(Collectors.toList());
    }

    private StreetAddress addressEntityToDeleveryAddress(OrderAddressEntity address) {
        return new StreetAddress(address.getId(), address.getStreet(), address.getPostalCode(), address.getCity());
    }

    private List<OrderItemEntity> orderItemsToOrderItemsEntities(List<OrderItem> items) {
        return items.stream().map(orderItem -> OrderItemEntity.builder()
                .id(orderItem.getId().getValue())
                .productId(orderItem.getProduct().getId().getValue())
                .price(orderItem.getPrice().getAmount())
                .quantity(orderItem.getQuantity())
                .subTotal(orderItem.getSubTotal().getAmount())
                .build()).collect(Collectors.toList());
    }

    private OrderAddressEntity deliveryAddressToAddressEntity(StreetAddress deliveryAddress) {
        return OrderAddressEntity.builder()
                .id(deliveryAddress.getId())
                .street(deliveryAddress.getStreet())
                .postalCode(deliveryAddress.getPostalCode())
                .city(deliveryAddress.getCity())
                .build();
    }
}
