package com.food.ordering.system.service.messaging.listener.kafka;

import com.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.CustomerAvroModel;
import com.food.ordering.system.order.service.domain.dto.message.CustomerModel;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.customer.CustomerMessageListener;
import com.food.ordering.system.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CustomerKafkaListener implements KafkaConsumer<CustomerAvroModel> {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final CustomerMessageListener customerMessageListener;

    public CustomerKafkaListener(OrderMessagingDataMapper orderMessagingDataMapper,
                                 CustomerMessageListener customerMessageListener) {
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.customerMessageListener = customerMessageListener;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.customer-group-id}",
                topics = "${order-service.customer-topic-name}")
    public void recevie(@Payload List<CustomerAvroModel> message,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> key,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("{} number of customer create message received with keys {}, partitions {} and offsets {}",
                message.size(),
                key.toString(),
                partitions.toString(),
                offsets.toString());
        message.forEach(customerAvroModel ->
                customerMessageListener.customerCreated(orderMessagingDataMapper.customerAvroModelToCustomerModel(customerAvroModel)));
    }
}
