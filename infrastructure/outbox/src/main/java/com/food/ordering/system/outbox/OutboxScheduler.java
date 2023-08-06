package com.food.ordering.system.outbox;

public interface OutboxScheduler {

    void processoutboxMessage();
}
