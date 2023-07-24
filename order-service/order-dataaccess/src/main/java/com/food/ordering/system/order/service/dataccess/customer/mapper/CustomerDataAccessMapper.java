package com.food.ordering.system.order.service.dataccess.customer.mapper;

import com.food.ordering.system.domain.valueObject.CustomerId;
import com.food.ordering.system.order.service.dataccess.customer.entity.CustomerEntity;
import com.food.ordering.system.order.service.domain.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataAccessMapper {

    public Customer customerEntityToCustomer(CustomerEntity customerEntity) {
        return new Customer(new CustomerId(customerEntity.getId()));
    }
}
