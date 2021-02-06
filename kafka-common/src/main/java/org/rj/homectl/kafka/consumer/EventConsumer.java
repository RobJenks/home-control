package org.rj.homectl.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.util.Map;

public interface EventConsumer<K, V> {

    void process(ConsumerRecords<K, V> records);

    Map<String, Object> consumerConfig();

    void shutdown();

}
