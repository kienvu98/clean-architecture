package com.food.ordering.system.payment.service.domain.outbox.scheduler;

import com.food.ordering.system.outbox.OutboxScheduler;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.payment.service.domain.outbox.model.OrderOutboxMessage;
import com.food.ordering.system.payment.service.domain.port.output.repository.OrderOutboxRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class OrderOutboxCleanerScheduler implements OutboxScheduler {

    private final OrderOutboxHelper orderOutboxHelper;

    public OrderOutboxCleanerScheduler(OrderOutboxHelper orderOutboxHelper) {
        this.orderOutboxHelper = orderOutboxHelper;
    }

    @Override
    @Transactional
    @Scheduled(cron = "@midnight")
    public void processoutboxMessage() {
        Optional<List<OrderOutboxMessage>> orderOutboxMessagesResponse = orderOutboxHelper
                .getOrderOutboxMessageByOutboxStatus(OutboxStatus.COMPLETED);
        if (orderOutboxMessagesResponse.isPresent() && orderOutboxMessagesResponse.get().size() > 0) {
            List<OrderOutboxMessage> orderOutboxMessages = orderOutboxMessagesResponse.get();
            log.info("Received {} OrderOutboxMessage for clean-up!", orderOutboxMessages.size());
            orderOutboxHelper.deleteOrderOutboxMessagebyOutboxStatus(OutboxStatus.COMPLETED);
            log.info("Deleted {} OrderOutboxMessage!", orderOutboxMessages.size());
        }
    }
}
