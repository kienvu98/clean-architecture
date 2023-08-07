package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static com.food.ordering.system.order.service.domain.entity.Order.FAILURE_MESSAGE_DELIMITER;

@Slf4j
@Validated
@Service
public class PaymentResponseMessageListenerImpl implements PaymentResponseMessageListener {

    private final OrderPaymentSaga paymentSaga;

    public PaymentResponseMessageListenerImpl(OrderPaymentSaga paymentSaga) {
        this.paymentSaga = paymentSaga;
    }

    @Override
    public void paymentCompleted(PaymentResponse paymentResponse) {
        paymentSaga.process(paymentResponse);
        log.info("Order Payment Saga process operation is completed for order id: {}", paymentResponse.getOrderId());
    }

    @Override
    public void paymentCancelled(PaymentResponse paymentResponse) {
        paymentSaga.rollback(paymentResponse);
        log.info("Order is roll bcked for order id {} with failure messages: {}",
                paymentResponse.getOrderId(),
                String.join(FAILURE_MESSAGE_DELIMITER, paymentResponse.getFailureMessage()));
    }
}
