package com.food.ordering.system.restaurant.service.domain;

import com.food.ordering.system.domain.valueObject.OrderId;
import com.food.ordering.system.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.food.ordering.system.restaurant.service.domain.entity.Restaurant;
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovalEvent;
import com.food.ordering.system.restaurant.service.domain.exception.RestaurantNotFoundException;
import com.food.ordering.system.restaurant.service.domain.mapper.RestaurantDataMapper;
import com.food.ordering.system.restaurant.service.domain.port.output.message.publisher.OrderApprovalMessagePublisher;
import com.food.ordering.system.restaurant.service.domain.port.output.message.publisher.OrderRejectMessagePublisher;
import com.food.ordering.system.restaurant.service.domain.port.output.repository.OrderApprovalRepository;
import com.food.ordering.system.restaurant.service.domain.port.output.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class RestaurantApprovalRequestHelper {

    private final RestaurantDomainService restaurantDomainService;
    private final RestaurantDataMapper restaurantDataMapper;
    private final OrderApprovalMessagePublisher orderApprovalMessagePublisher;
    private final OrderRejectMessagePublisher orderRejectMessagePublisher;
    private final OrderApprovalRepository orderApprovalRepository;
    private final RestaurantRepository restaurantRepository;

    public RestaurantApprovalRequestHelper(RestaurantDomainService restaurantDomainService,
                                           RestaurantDataMapper restaurantDataMapper,
                                           OrderApprovalMessagePublisher orderApprovalMessagePublisher,
                                           OrderRejectMessagePublisher orderRejectMessagePublisher,
                                           OrderApprovalRepository orderApprovalRepository,
                                           RestaurantRepository restaurantRepository) {
        this.restaurantDomainService = restaurantDomainService;
        this.restaurantDataMapper = restaurantDataMapper;
        this.orderApprovalMessagePublisher = orderApprovalMessagePublisher;
        this.orderRejectMessagePublisher = orderRejectMessagePublisher;
        this.orderApprovalRepository = orderApprovalRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public OrderApprovalEvent persistOrderApproval(RestaurantApprovalRequest restaurantApprovalRequest) {
        log.info("Processing restaurant approval for order id: {}", restaurantApprovalRequest.getOrderId());
        List<String> failureMessage = new ArrayList<>();
        Restaurant restaurant = findRestaurant(restaurantApprovalRequest);
        OrderApprovalEvent orderApprovalEvent = restaurantDomainService.valiadateOrder(restaurant, failureMessage,
                orderApprovalMessagePublisher, orderRejectMessagePublisher);
        orderApprovalRepository.save(restaurant.getOrderApproval());
        return orderApprovalEvent;
    }

    private Restaurant findRestaurant(RestaurantApprovalRequest restaurantApprovalRequest) {
        Restaurant restaurant = restaurantDataMapper.restaurantApprovalRequestModelToRestaurant(restaurantApprovalRequest);
        Optional<Restaurant> resultRestaunrant = restaurantRepository.findRestaurantInformation(restaurant);
        if (resultRestaunrant.isEmpty()) {
            log.error("Restaurant with id " + restaurant.getId().getValue() + " not found!");
            throw new RestaurantNotFoundException("Restaurant with id " + restaurant.getId().getValue() + " not found!");
        }

        Restaurant restaurantEntity = resultRestaunrant.get();
        restaurant.setActive(restaurantEntity.isActive());
        restaurant.getOrderDetail().getProducts().forEach(product ->
                restaurantEntity.getOrderDetail().getProducts().forEach(productEntiy -> {
                    if (productEntiy.getId().equals(product.getId())) {
                        product.updateWithConfirmeNamePriceAndAvailability(productEntiy.getName(), productEntiy.getPrice(), productEntiy.isAvailable());
                    }
                }));
        restaurant.getOrderDetail().setId(new OrderId(UUID.fromString(restaurantApprovalRequest.getOrderId())));
        return  restaurant;
    }
}

