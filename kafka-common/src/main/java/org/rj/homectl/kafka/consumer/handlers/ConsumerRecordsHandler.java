package org.rj.homectl.kafka.consumer.handlers;

import org.apache.kafka.clients.consumer.ConsumerRecords;


public interface ConsumerRecordsHandler<K, V> {

    void process(ConsumerRecords<K, V> consumerRecords);

}
