package com.food.ordering.system.payment.service.messaging.mapper;

import com.food.ordering.system.domain.valueObject.PaymentOrderStatus;
import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.kafka.order.avro.model.PaymentStatus;
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.event.PaymentCancelledEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentCompleteEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentFalledEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentMessagingDataMapper {

    public PaymentResponseAvroModel paymentCompleteEventtoPaymentResponseAvroModel(PaymentCompleteEvent paymentCompleteEvent) {
        return PaymentResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setPaymentId(paymentCompleteEvent.getPayment().getId().getValue().toString())
                .setCustomerId(paymentCompleteEvent.getPayment().getCustomerId().getValue().toString())
                .setOrderId(paymentCompleteEvent.getPayment().getOrderId().getValue().toString())
                .setPrice(paymentCompleteEvent.getPayment().getPrice().getAmount())
                .setCreatedAt(paymentCompleteEvent.getPayment().getCreatedAt().toInstant())
                .setFailureMessages(paymentCompleteEvent.getFailureMessage())
                .setPaymentStatus(PaymentStatus.valueOf(paymentCompleteEvent.getPayment().getPaymentStatus().name()))
                .build();
    }

    public PaymentResponseAvroModel paymentCancelledEventtoPaymentResponseAvroModel(PaymentCancelledEvent paymentCancelledEvent) {
        return PaymentResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setPaymentId(paymentCancelledEvent.getPayment().getId().getValue().toString())
                .setCustomerId(paymentCancelledEvent.getPayment().getCustomerId().getValue().toString())
                .setOrderId(paymentCancelledEvent.getPayment().getOrderId().getValue().toString())
                .setPrice(paymentCancelledEvent.getPayment().getPrice().getAmount())
                .setCreatedAt(paymentCancelledEvent.getPayment().getCreatedAt().toInstant())
                .setFailureMessages(paymentCancelledEvent.getFailureMessage())
                .setPaymentStatus(PaymentStatus.valueOf(paymentCancelledEvent.getPayment().getPaymentStatus().name()))
                .build();
    }

    public PaymentResponseAvroModel paymentFailedEventtoPaymentResponseAvroModel(PaymentFalledEvent paymentFalledEvent) {
        return PaymentResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setPaymentId(paymentFalledEvent.getPayment().getId().getValue().toString())
                .setCustomerId(paymentFalledEvent.getPayment().getCustomerId().getValue().toString())
                .setOrderId(paymentFalledEvent.getPayment().getOrderId().getValue().toString())
                .setPrice(paymentFalledEvent.getPayment().getPrice().getAmount())
                .setCreatedAt(paymentFalledEvent.getPayment().getCreatedAt().toInstant())
                .setFailureMessages(paymentFalledEvent.getFailureMessage())
                .setPaymentStatus(PaymentStatus.valueOf(paymentFalledEvent.getPayment().getPaymentStatus().name()))
                .build();
    }

    public PaymentRequest paymentRequestAvroModelToPaymentRequest(PaymentRequestAvroModel paymentRequestAvroModel) {
        return PaymentRequest.builder()
                .id(paymentRequestAvroModel.getId())
                .sagaid(paymentRequestAvroModel.getSagaId())
                .customerId(paymentRequestAvroModel.getCustomerId())
                .orderId(paymentRequestAvroModel.getOrderId())
                .price(paymentRequestAvroModel.getPrice())
                .createdAt(paymentRequestAvroModel.getCreatedAt())
                .paymentOrderStatus(PaymentOrderStatus.valueOf(paymentRequestAvroModel.getPaymentOrderStatus().name()))
                .build();
    }

}
