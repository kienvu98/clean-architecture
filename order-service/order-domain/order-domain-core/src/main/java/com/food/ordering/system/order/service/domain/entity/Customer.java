package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.enitty.AggregateRoot;
import com.food.ordering.system.domain.valueObject.CustomerId;

public class Customer extends AggregateRoot<CustomerId> {

    private String userName;
    private String firstName;
    private String lastName;

    public Customer(CustomerId customerId,String userName, String firstName, String lastName) {
        super.setId(customerId);
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Customer(CustomerId customerId) {
        super.setId(customerId);
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
