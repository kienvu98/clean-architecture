package com.food.ordering.system.restaurant.service.messaging.listener.kafka;

import com.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordering.system.restaurant.service.domain.RestaurantApprovalRequestMessageListenerImpl;
import com.food.ordering.system.restaurant.service.domain.exception.RestaurantDomainException;
import com.food.ordering.system.restaurant.service.domain.exception.RestaurantNotFoundException;
import com.food.ordering.system.restaurant.service.messaging.mapper.RestaurantMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLState;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
public class RestaurantApprovalRequestKafkaListener implements KafkaConsumer<RestaurantApprovalRequestAvroModel> {

    private final RestaurantApprovalRequestMessageListenerImpl restaurantApprovalRequestMessageListener;
    private final RestaurantMessagingDataMapper restaurantMessagingDataMapper;

    public RestaurantApprovalRequestKafkaListener(RestaurantApprovalRequestMessageListenerImpl restaurantApprovalRequestMessageListener,
                                                  RestaurantMessagingDataMapper restaurantMessagingDataMapper) {
        this.restaurantApprovalRequestMessageListener = restaurantApprovalRequestMessageListener;
        this.restaurantMessagingDataMapper = restaurantMessagingDataMapper;
    }

    @KafkaListener(id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}",
            topics = "${restaurant-service.restaurant-approval-request-topic-name}")
    @Override
    public void recevie(@Payload List<RestaurantApprovalRequestAvroModel> message,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> key,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("{} number of order approval request received with keys {}, partititon {} and offsets {}, " +
                        " sending for restaurant approval",
                message.size(),
                key.toString(),
                partitions.toString(),
                offsets.toString());
        message.forEach(restaurantApprovalRequestAvroModel -> {
            try {
                log.info("Processing order approval for order id: {}", restaurantApprovalRequestAvroModel.getOrderId());
                restaurantApprovalRequestMessageListener.approvedOrder(restaurantMessagingDataMapper
                        .restaurantApprovalRequestModelToRestaurantApproval(restaurantApprovalRequestAvroModel));
            } catch (DataAccessException e) {
                SQLException sqlException = (SQLException) e.getRootCause();
                if (sqlException != null && sqlException.getSQLState() != null &&
                        PSQLState.UNIQUE_VIOLATION.getState().equals(sqlException.getSQLState())) {
                    log.error("Caught unique constraint excepption with sql state: {} in RestaurantApprovalRequestKafkaListenre for order id: {}",
                            sqlException.getSQLState(), restaurantApprovalRequestAvroModel.getOrderId());
                } else {
                    throw new RestaurantDomainException("Throwing DataAccessException in RestaurantApprovalRequestKafkaListenre" + e.getMessage());
                }
            } catch (RestaurantNotFoundException e) {
                log.error("No restaurant found for restaurant id: {}, and order id : {}",
                        restaurantApprovalRequestAvroModel.getRestaurantId(),
                        restaurantApprovalRequestAvroModel.getOrderId());
            }
        });
    }
}
