package com.food.ordering.system.payment.service.messaging.publisher.kafka;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.kafka.producer.KafkaMessageHelper;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import com.food.ordering.system.payment.service.domain.config.PaymentServiceConfigData;
import com.food.ordering.system.payment.service.domain.event.PaymentFalledEvent;
import com.food.ordering.system.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentFailedKafkaMessagePublisher implements DomainEventPublisher<PaymentFalledEvent> {
    private final KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer;

    private final PaymentMessagingDataMapper paymentMessagingDataMapper;

    private final PaymentServiceConfigData paymentServiceConfigData;

    private final KafkaMessageHelper kafkaMessageHelper;

    public PaymentFailedKafkaMessagePublisher(KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer,
                                                 PaymentMessagingDataMapper paymentMessagingDataMapper,
                                                 PaymentServiceConfigData paymentServiceConfigData,
                                                 KafkaMessageHelper kafkaMessageHelper) {
        this.kafkaProducer = kafkaProducer;
        this.paymentMessagingDataMapper = paymentMessagingDataMapper;
        this.paymentServiceConfigData = paymentServiceConfigData;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    @Override
    public void publisher(PaymentFalledEvent domainEvent) {
        String orderId = domainEvent.getPayment().getOrderId().getValue().toString();
        log.info("Received PaymentEvent for order i: {}", orderId);
        try {
            PaymentResponseAvroModel paymentResponseAvroModel = paymentMessagingDataMapper
                    .paymentFailedEventtoPaymentResponseAvroModel(domainEvent);

            kafkaProducer.send(paymentServiceConfigData.getPaymentResponseTopicName(),
                    orderId,
                    paymentResponseAvroModel,
                    kafkaMessageHelper.getKafkaCallback(paymentServiceConfigData.getPaymentResponseTopicName(),
                            paymentResponseAvroModel, orderId, "PaymentResponseAvroModel"));
            log.info("PaymentResponseAvroModel sent to Kafka for order id : {}", paymentResponseAvroModel.getId());
        } catch (Exception e) {
            log.error("Error while sending PaymentResponseAvroModel message" +
                    " to kafka with order if : {}, error : {}", orderId, e.getMessage());
        }
    }
}
