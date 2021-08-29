package org.rj.homectl.kafka.consumer.handlers;

public interface RecordInfoConsumer<K, V> {
    void acceptRecord(final ConsumerRecordInfo<K, V> recordInfo);
}
