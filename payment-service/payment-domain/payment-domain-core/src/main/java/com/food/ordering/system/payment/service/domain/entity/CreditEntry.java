package com.food.ordering.system.payment.service.domain.entity;

import com.food.ordering.system.domain.enitty.BaseEntity;
import com.food.ordering.system.domain.valueObject.CustomerId;
import com.food.ordering.system.domain.valueObject.Money;
import com.food.ordering.system.payment.service.domain.valueobject.CreditEntyId;

public class CreditEntry extends BaseEntity<CreditEntyId> {

    private final CustomerId customerId;
    private Money totalCreditAmount;

    // them tien trong khoan tin dung
    public void addCreditAmount(Money amount) {
        totalCreditAmount.add(amount);
    }

    // tru tien trong khoan tin dung
    public void subtractCreditAmount(Money amount) {
        totalCreditAmount = totalCreditAmount.sub(amount);
    }

    private CreditEntry(Builder builder) {
        setId(builder.creditEntyId);
        customerId = builder.customerId;
        totalCreditAmount = builder.totalCreditAmount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public Money getTotalCreditAmount() {
        return totalCreditAmount;
    }

    public static final class Builder {
        private CreditEntyId creditEntyId;
        private CustomerId customerId;
        private Money totalCreditAmount;

        private Builder() {
        }

        public Builder creditEntyId(CreditEntyId val) {
            creditEntyId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder totalCreditAmount(Money val) {
            totalCreditAmount = val;
            return this;
        }

        public CreditEntry build() {
            return new CreditEntry(this);
        }
    }
}
