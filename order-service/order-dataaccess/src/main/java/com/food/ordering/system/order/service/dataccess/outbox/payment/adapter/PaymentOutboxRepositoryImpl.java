package com.food.ordering.system.order.service.dataccess.outbox.payment.adapter;

import com.food.ordering.system.order.service.dataccess.outbox.payment.entity.PaymentOutboxEntity;
import com.food.ordering.system.order.service.dataccess.outbox.payment.exception.PaymentOutboxNotFoundExcetion;
import com.food.ordering.system.order.service.dataccess.outbox.payment.mapper.PaymentOutboxDataAccessMapper;
import com.food.ordering.system.order.service.dataccess.outbox.payment.repository.PaymentOutboxJpaRepository;
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.ordering.system.order.service.domain.ports.output.repository.PaymentOutboxRepository;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.saga.SagaStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PaymentOutboxRepositoryImpl implements PaymentOutboxRepository {

    private final PaymentOutboxDataAccessMapper paymentOutboxDataAccessMapper;
    private final PaymentOutboxJpaRepository paymentOutboxJpaRepository;

    public PaymentOutboxRepositoryImpl(PaymentOutboxDataAccessMapper paymentOutboxDataAccessMapper,
                                       PaymentOutboxJpaRepository paymentOutboxJpaRepository) {
        this.paymentOutboxDataAccessMapper = paymentOutboxDataAccessMapper;
        this.paymentOutboxJpaRepository = paymentOutboxJpaRepository;
    }

    @Override
    public OrderPaymentOutboxMessage save(OrderPaymentOutboxMessage orderPaymentOutboxMessage) {
        return paymentOutboxDataAccessMapper.paymentOutboxEntityToOrderPaymentOutboxMessage(paymentOutboxJpaRepository.
                save(paymentOutboxDataAccessMapper.paymentOutboxMessageToPaymentOutboxEntity(orderPaymentOutboxMessage)));
    }

    @Override
    public Optional<List<OrderPaymentOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatus(String type, OutboxStatus outboxStatus, SagaStatus... sagastatus) {
        return Optional.of(paymentOutboxJpaRepository.findByTypeAndOutboxStatusAndSagaStatusIn(type,
                outboxStatus, Arrays.asList(sagastatus)).orElseThrow(() -> new PaymentOutboxNotFoundExcetion("Payment outbox object " +
                "could not found for saga type " + type))
                .stream()
                .map(paymentOutboxDataAccessMapper::paymentOutboxEntityToOrderPaymentOutboxMessage)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<OrderPaymentOutboxMessage> findByTypeAndSagaIdAndSagaStatus(String type, UUID sagaId, SagaStatus... sagastatus) {
        return paymentOutboxJpaRepository.findByTypeAndSagaIdAndSagaStatusIn(type, sagaId, Arrays.asList(sagastatus))
                .map(paymentOutboxDataAccessMapper::paymentOutboxEntityToOrderPaymentOutboxMessage);
    }

    @Override
    public void deleteByTypeAndOutboxStatusAndSagaStatus(String type, OutboxStatus outboxStatus, SagaStatus... sagastatus) {
        paymentOutboxJpaRepository.deleteByTypeAndOutboxStatusAndSagaStatusIn(type, outboxStatus, Arrays.asList(sagastatus));
    }
}
