package com.food.ordering.system.kafka.consumer;

import org.apache.avro.specific.SpecificRecordBase;

import java.util.List;

public interface KafkaConsumer<T extends SpecificRecordBase> {
    void recevie(List<T> message, List<Long> key, List<Integer> partitions, List<Long> offsets);
}
