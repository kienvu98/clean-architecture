package com.food.ordering.system.domain.valueObject;

import com.food.ordering.system.domain.enitty.BaseEntity;

import java.util.UUID;

public class OrderId extends BaseId<UUID> {
    public OrderId(UUID value) {
        super(value);
    }
}
