package com.food.ordering.system.restaurant.service.domain.outbox.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OrderEventPayload {
    @JsonProperty
    private String orderId;
    @JsonProperty
    private String restuarantId;
    @JsonProperty
    private ZonedDateTime createdAt;
    @JsonProperty
    private String orderApprovalStatus;
    @JsonProperty
    private List<String> failureMessage;
}
