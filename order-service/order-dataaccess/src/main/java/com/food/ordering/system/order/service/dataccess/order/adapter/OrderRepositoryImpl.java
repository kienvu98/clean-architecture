package com.food.ordering.system.order.service.dataccess.order.adapter;

import com.food.ordering.system.domain.valueObject.OrderId;
import com.food.ordering.system.order.service.dataccess.order.entity.OrderEntity;
import com.food.ordering.system.order.service.dataccess.order.mapper.OrderDataAccessMapper;
import com.food.ordering.system.order.service.dataccess.order.repository.OrderJpaRepository;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class OrderRepositoryImpl implements OrderRepository {

    private OrderJpaRepository orderJpaRepository;
    private OrderDataAccessMapper orderDataAccessMapper;

    public OrderRepositoryImpl(OrderJpaRepository orderJpaRepository,
                              OrderDataAccessMapper orderDataAccessMapper) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderDataAccessMapper = orderDataAccessMapper;
    }

    @Override
    public Order save(Order order) {
        OrderEntity entity = orderDataAccessMapper.orderToOrderEntity(order);
        OrderEntity entitySave = orderJpaRepository.save(entity);
        Order orderSave = orderDataAccessMapper.orderEntityToOrder(entitySave);
        return orderSave;
    }

    @Override
    public Optional<Order> findByTrackingId(TrackingId trackingId) {
        return orderJpaRepository.findByTrackingId(trackingId.getValue())
                .map(orderDataAccessMapper::orderEntityToOrder);
    }

    @Override
    public Optional<Order> findById(OrderId orderId) {
        return orderJpaRepository.findById(orderId.getValue()).map(orderDataAccessMapper::orderEntityToOrder);
    }
}
