package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.enitty.AggregateRoot;
import com.food.ordering.system.domain.valueObject.*;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.valueobject.OrderItemId;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Order extends AggregateRoot<OrderId> {
    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress deliveryAddress;
    private final Money price;
    private final List<OrderItem> items;

    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    public static final String FAILURE_MESSAGE_DELIMITER = ",";

    // khởi tạo order
    public void inintializerOrder(){
        setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = OrderStatus.PENDING;
        inintializerOrderItems();
    }

    // khởi tạo các orderitem ứng với order
    private void inintializerOrderItems() {
        long itemId = 1;
        for (OrderItem orderItem: items){
            orderItem.initializerOrderItem(super.getId(), new OrderItemId(itemId++));
        }
    }

    // validate order
    public void validateOrder(){
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    private void validateTotalPrice() {
        if (price == null || !price.isGreaterThanZero()){
            throw new OrderDomainException("Total price must be greater than zero!");
        }
    }

    private void validateInitialOrder() {
        if (orderStatus != null && getId() != null){
            throw new OrderDomainException("Order is not in correct state for inintialization");
        }
    }

    private void validateItemsPrice() {
        Money orderItemsTotal = items.stream().map(orderItem -> {
            validateItemPrice(orderItem);
            return orderItem.getSubTotal();
        }).reduce(Money.ZERO, Money::add);

        if (!price.equals(orderItemsTotal)){
            throw new OrderDomainException("Total price: " + price.getAmount()
                    + "is not equal to order items total: " + orderItemsTotal.getAmount() + "!");
        }
    }

    private void validateItemPrice(OrderItem orderItem) {
        boolean test = orderItem.isPriceValid();
        if (!test){
            throw new OrderDomainException("Order item price: " + orderItem.getPrice().getAmount() +
                    "is not valid for product" + orderItem.getProduct().getId().getValue());
        }
    }

    // function thanh toán đơn hàng(chuyển trạng thái từ chờ thanh toán sang thanh toán)
    public void pay(){
        if(orderStatus != OrderStatus.PENDING){
            throw new OrderDomainException("Order is not in correct state for pay operation!");
        }
        // đơn hàng chờ thanh toán thì chuyển trạng thái thanh toán
        orderStatus = OrderStatus.PAID;
    }

    // function phê duyệt đơn hàng(chuyển từ trạng thái thanh toán sang trạng thái  duyệt đơn hàng)
    public void approve(){
        if(orderStatus != OrderStatus.PAID){
            throw new OrderDomainException("Order is not in correct state for approve operation!");
        }
        orderStatus = OrderStatus.APPROVED;
    }

    // function chuyển về trạng thái đang hủy đơn hàng
    public void initCancel(List<String> failureMessages){
        if(orderStatus != OrderStatus.PAID){
            throw new OrderDomainException("Order is not in correct state for initCancel operation!");
        }
        orderStatus = OrderStatus.CANCELLING;
        updateFailureMessages(failureMessages);
    }

    // function hủy đơn hàng(chuyển từ trạng thái đang xử lý hủy thành trạng thái hủy đơn hàng)
    public void cancel(List<String> failureMessages){
        if (!(orderStatus == OrderStatus.PENDING || orderStatus == OrderStatus.CANCELLING)){
            throw new OrderDomainException("Order is not in correct state for cancel operation!");
        }
        orderStatus = OrderStatus.CANCELLED;
        updateFailureMessages(failureMessages);
    }

    private void updateFailureMessages(List<String> failureMessages) {
        if (this.failureMessages != null && failureMessages != null){
            this.failureMessages.addAll(failureMessages.stream().filter(message -> message.isEmpty()).toList());
        }
        if(this.failureMessages == null){
            this.failureMessages = failureMessages;
        }
    }


    private Order(Builder builder) {
        super.setId(builder.orderId);
        customerId = builder.customerId;
        restaurantId = builder.restaurantId;
        deliveryAddress = builder.deliveryAddress;
        price = builder.price;
        items = builder.items;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessages = builder.failureMessages;
    }

    public static Builder builder() {
        return new Builder();
    }


    public CustomerId getCustomerId() {
        return customerId;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public StreetAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    public static final class Builder {
        private OrderId orderId;
        private CustomerId customerId;
        private RestaurantId restaurantId;
        private StreetAddress deliveryAddress;
        private Money price;
        private List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;

        private Builder() {
        }

        public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder deliveryAddress(StreetAddress val) {
            deliveryAddress = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder items(List<OrderItem> val) {
            items = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
