package org.rj.homectl.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecords;

public interface EventConsumer<K, V> {

    void process(ConsumerRecords<K, V> records);

    void shutdown();

}
