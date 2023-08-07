package com.food.ordering.system.service.messaging.publisher.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordering.system.kafka.producer.KafkaMessageHelper;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import com.food.ordering.system.order.service.domain.config.OrderServiceConfigData;
import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalEventPayload;
import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentEventPayload;
import com.food.ordering.system.order.service.domain.ports.output.message.pulisher.restaurantapproval.RestaurantApprovalRequestMessagePublisher;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Slf4j
@Component
public class OrderApprovalEventPublisher implements RestaurantApprovalRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaMessageHelper kafKafkaMessageHelper;

    public OrderApprovalEventPublisher(OrderMessagingDataMapper orderMessagingDataMapper,
                                       KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer,
                                       OrderServiceConfigData orderServiceConfigData,
                                       KafkaMessageHelper kafKafkaMessageHelper) {
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.kafkaProducer = kafkaProducer;
        this.orderServiceConfigData = orderServiceConfigData;
        this.kafKafkaMessageHelper = kafKafkaMessageHelper;
    }

    @Override
    public void publish(OrderApprovalOutboxMessage orderApprovalOutboxMessage, BiConsumer<OrderApprovalOutboxMessage, OutboxStatus> outboxCallback) {
        OrderApprovalEventPayload orderApprovalEventPayload = kafKafkaMessageHelper
                .getOrderEventPayload(orderApprovalOutboxMessage.getPayload(), OrderApprovalEventPayload.class);
        String sagaId = orderApprovalOutboxMessage.getSagaId().toString();

        try {
            log.info("Received OrderApprovalOutboxMessage for order id: {} and saga id: {}",
                    orderApprovalEventPayload.getOrderId(), sagaId);
            RestaurantApprovalRequestAvroModel restaurantApprovalRequestAvroModel = orderMessagingDataMapper
                    .orderApprovalEventToRestaurantApprovalRequestAvroModel(sagaId, orderApprovalEventPayload);

            kafkaProducer.send(orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
                    sagaId,
                    restaurantApprovalRequestAvroModel,
                    kafKafkaMessageHelper.getKafkaCallback(orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
                            restaurantApprovalRequestAvroModel,
                            orderApprovalOutboxMessage,
                            outboxCallback,
                            orderApprovalEventPayload.getOrderId(),
                            "RestaurantApprovalRequestAvroModel"));
            log.info("OrderApprovalEventPayload sent to kafka for order id: {} and saga id: {}",
                    restaurantApprovalRequestAvroModel.getOrderId(), sagaId);
        } catch (Exception e) {
            log.error("Error while sending OrderApprovalEventPayload to kafka for order id: {} and saga id: {}, error",
                    orderApprovalEventPayload.getOrderId(), sagaId, e.getMessage());
        }
    }
}
