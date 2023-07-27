package com.food.ordering.system.payment.service.domain;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.domain.valueObject.Money;
import com.food.ordering.system.domain.valueObject.PaymentStatus;
import com.food.ordering.system.payment.service.domain.entity.CreditEntry;
import com.food.ordering.system.payment.service.domain.entity.CreditHistory;
import com.food.ordering.system.payment.service.domain.entity.Payment;
import com.food.ordering.system.payment.service.domain.event.PaymentCancelledEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentCompleteEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentFalledEvent;
import com.food.ordering.system.payment.service.domain.valueobject.CreditHistoryId;
import com.food.ordering.system.payment.service.domain.valueobject.TransactionType;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static com.food.ordering.system.domain.DomainConstants.UTC;

@Slf4j
public class PaymentDomainSerivceImpl implements PaymentDomainService{

    @Override
    public PaymentEvent validateAndInitiatePayment(Payment payment,
                                                   CreditEntry creditEntry,
                                                   List<CreditHistory> creditHistories,
                                                   List<String> failureMessage,
                                                   DomainEventPublisher<PaymentCompleteEvent> paymentCompleteEventDomainEventPublisher,
                                                   DomainEventPublisher<PaymentFalledEvent> paymentFalledEventDomainEventPublisher) {
        payment.validatePayment(failureMessage);
        payment.initializePayment();
        // validate credit
        validateCreditEtry(payment, creditEntry, failureMessage);
        subtractCreditEntry(payment, creditEntry);
        updateCreditHistory(payment, creditHistories, TransactionType.DEBIT);
        validateCreditHistory(creditEntry, creditHistories, failureMessage);
        if (failureMessage.isEmpty()) {
            // hoan thanh thanh toan
            log.info("Payment is initiated for order id : {}", payment.getOrderId().getValue());
            payment.updatePayment(PaymentStatus.COMPLETED);
            return new PaymentCompleteEvent(payment, ZonedDateTime.now(ZoneId.of(UTC)), paymentCompleteEventDomainEventPublisher);
        } else {
            // thanh toan that bai
            log.info("Payment is failed for order id : {}", payment.getOrderId().getValue());
            payment.updatePayment(PaymentStatus.FAILED);
            return new PaymentFalledEvent(payment, ZonedDateTime.now(ZoneId.of(UTC)), failureMessage, paymentFalledEventDomainEventPublisher);
        }
    }

    @Override
    public PaymentEvent validateAndCancelPayment(Payment payment,
                                                 CreditEntry creditEntry,
                                                 List<CreditHistory> creditHistories,
                                                 List<String> failureMessage,
                                                 DomainEventPublisher<PaymentCancelledEvent> paymentCancelledEventDomainEventPublisher,
                                                 DomainEventPublisher<PaymentFalledEvent> paymentFalledEventDomainEventPublisher) {
        payment.validatePayment(failureMessage);
        addCreditEntry(payment, creditEntry);
        updateCreditHistory(payment, creditHistories,TransactionType.CREDIT);
        if (failureMessage.isEmpty()) {
            log.info("Payment is cancelled for order id : {}", payment.getOrderId().getValue());
            payment.updatePayment(PaymentStatus.CANCELLED);
            return new PaymentCancelledEvent(payment,  ZonedDateTime.now(ZoneId.of(UTC)), paymentCancelledEventDomainEventPublisher);
        }
        else {
            log.info("Payment cancellation is failed for order id : {}", payment.getOrderId().getValue());
            payment.updatePayment(PaymentStatus.FAILED);
            return new PaymentFalledEvent(payment,  ZonedDateTime.now(ZoneId.of(UTC)), failureMessage, paymentFalledEventDomainEventPublisher);
        }
    }

    // kiem tra so tien thanh toan va khoang tin dung cua khach hang
    private void validateCreditEtry(Payment payment, CreditEntry creditEntry, List<String> failureMessage) {
        if (payment.getPrice().isGreaterThan(creditEntry.getTotalCreditAmount())) {
            log.error("Customer with id : {} doesn't have enough credit for payment!", payment.getCustomerId().getValue());
            failureMessage.add("Customer with id=" + payment.getCustomerId().getValue() +" doesn't have enough credit for payment!");
        }
    }

    // tru tien thanh toan trong tin dung khach hang
    private void subtractCreditEntry(Payment payment, CreditEntry creditEntry) {
        creditEntry.subtractCreditAmount(payment.getPrice());
    }

    // them vao lich su thanh toan
    private void updateCreditHistory(Payment payment, List<CreditHistory> creditHistories, TransactionType transactionType) {
        creditHistories.add(CreditHistory.builder()
                        .creditHistoryId(new CreditHistoryId(UUID.randomUUID()))
                        .customerId(payment.getCustomerId())
                        .amount(payment.getPrice())
                        .transactionType(transactionType)
                .build());
    }

    // valdte lich su thanh toan
    private void validateCreditHistory(CreditEntry creditEntry, List<CreditHistory> creditHistories, List<String> failureMessage) {
        Money totalCreditHistory = getToTalHistoryAmount(creditHistories, TransactionType.CREDIT);
        Money totalDebitHistory = getToTalHistoryAmount(creditHistories, TransactionType.DEBIT);
        if (totalDebitHistory.isGreaterThan(totalCreditHistory)) {
            log.info("Customer with id: {} doens't have enough credit according to credit history",
                    creditEntry.getCustomerId().getValue());
            failureMessage.add("Customer with id=" +  creditEntry.getCustomerId().getValue() +  " doens't have enough credit according to credit history");
        }
        if (creditEntry.getTotalCreditAmount().equals(totalCreditHistory.sub(totalDebitHistory))) {
            log.info("credit history total is not equal to current credit for customer id: {}",
                    creditEntry.getCustomerId().getValue());
            failureMessage.add("credit history total is not equal to current credit for customer id: " +
                    creditEntry.getCustomerId().getValue());
        }
    }

    private static Money getToTalHistoryAmount(List<CreditHistory> creditHistories, TransactionType transactionType) {
        return creditHistories.stream()
                .filter(creditHistory -> creditHistory.getTransactionType() == transactionType)
                .map(CreditHistory::getAmount)
                .reduce(Money.ZERO, Money::add);
    }

    // them tro lai so tien trong tin dung khach hang
    private void addCreditEntry(Payment payment, CreditEntry creditEntry) {
        creditEntry.addCreditAmount(payment.getPrice());
    }
}
