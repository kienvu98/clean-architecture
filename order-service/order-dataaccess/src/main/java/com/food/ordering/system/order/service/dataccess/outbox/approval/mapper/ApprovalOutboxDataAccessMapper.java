package com.food.ordering.system.order.service.dataccess.outbox.approval.mapper;

import com.food.ordering.system.order.service.dataccess.outbox.approval.entity.ApprovalOutboxEntity;
import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import org.springframework.stereotype.Component;

@Component
public class ApprovalOutboxDataAccessMapper {

    public ApprovalOutboxEntity orderApprovalOutboxMessageToApprovalOutboxEntity(OrderApprovalOutboxMessage orderApprovalOutboxMessage) {
        return ApprovalOutboxEntity.builder()
                .id(orderApprovalOutboxMessage.getId())
                .sagaId(orderApprovalOutboxMessage.getSagaId())
                .type(orderApprovalOutboxMessage.getType())
                .createdAt(orderApprovalOutboxMessage.getCreatedAt())
                .payload(orderApprovalOutboxMessage.getPayload())
                .outboxStatus(orderApprovalOutboxMessage.getOutboxStatus())
                .sagaStatus(orderApprovalOutboxMessage.getSagaStatus())
                .orderStatus(orderApprovalOutboxMessage.getOrderStatus())
                .version(orderApprovalOutboxMessage.getVersion())
                .build();
    }

    public OrderApprovalOutboxMessage approvalOutboxEntityToOrderApprovalOutboxMessage(ApprovalOutboxEntity approvalOutboxEntity) {
        return OrderApprovalOutboxMessage.builder()
                .id(approvalOutboxEntity.getId())
                .sagaId(approvalOutboxEntity.getSagaId())
                .createdAt(approvalOutboxEntity.getCreatedAt())
                .type(approvalOutboxEntity.getType())
                .payload(approvalOutboxEntity.getPayload())
                .outboxStatus(approvalOutboxEntity.getOutboxStatus())
                .sagaStatus(approvalOutboxEntity.getSagaStatus())
                .orderStatus(approvalOutboxEntity.getOrderStatus())
                .version(approvalOutboxEntity.getVersion())
                .build();
    }
}
