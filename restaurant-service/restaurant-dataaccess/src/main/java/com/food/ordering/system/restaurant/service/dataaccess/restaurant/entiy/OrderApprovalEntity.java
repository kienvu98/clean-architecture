package com.food.ordering.system.restaurant.service.dataaccess.restaurant.entiy;

import com.food.ordering.system.domain.valueObject.OrderApprovalStatus;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_approval", schema = "restaurant")
public class OrderApprovalEntity {

    @Id
    private UUID id;
    private UUID restaurantId;
    private UUID orderId;
    @Enumerated(EnumType.STRING)
    private OrderApprovalStatus status;

}
