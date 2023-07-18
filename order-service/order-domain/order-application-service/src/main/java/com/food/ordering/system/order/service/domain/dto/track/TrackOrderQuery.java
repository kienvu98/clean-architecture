package com.food.ordering.system.order.service.domain.dto.track;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class TrackOrderQuery { // theo doi truy van trang thai moi nhat cua don hang
    private final UUID orderTrackingId;
}
