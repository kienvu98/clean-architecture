package com.food.ordering.system.payment.service.domain.valueobject;

import com.food.ordering.system.domain.valueObject.BaseId;

import java.util.UUID;

public class CreditEntyId extends BaseId<UUID> {
    public CreditEntyId(UUID value) {
        super(value);
    }
}
