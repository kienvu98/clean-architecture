package com.food.ordering.system.restaurant.service.dataaccess.restaurant.adapter;

import com.food.ordering.system.restaurant.service.dataaccess.restaurant.mapper.RestaurantDataAccessMapper;
import com.food.ordering.system.restaurant.service.dataaccess.restaurant.respository.OrderApprovalJpaRepository;
import com.food.ordering.system.restaurant.service.domain.entity.OrderApproval;
import com.food.ordering.system.restaurant.service.domain.port.output.repository.OrderApprovalRepository;
import org.springframework.stereotype.Component;

@Component
public class OrdderApprovalRepositoryImpl implements OrderApprovalRepository {

    private final RestaurantDataAccessMapper restaurantDataAccessMapper;

    private final OrderApprovalJpaRepository orderApprovalJpaRepository;

    public OrdderApprovalRepositoryImpl(RestaurantDataAccessMapper restaurantDataAccessMapper,
                                        OrderApprovalJpaRepository orderApprovalJpaRepository) {
        this.restaurantDataAccessMapper = restaurantDataAccessMapper;
        this.orderApprovalJpaRepository = orderApprovalJpaRepository;
    }

    @Override
    public OrderApproval save(OrderApproval orderApproval) {
        return restaurantDataAccessMapper.
                orderApprovalEntityToOrderApproval(orderApprovalJpaRepository.
                        save(restaurantDataAccessMapper.orderApprovalToOrderApprovalEntity(orderApproval)));
    }
}
