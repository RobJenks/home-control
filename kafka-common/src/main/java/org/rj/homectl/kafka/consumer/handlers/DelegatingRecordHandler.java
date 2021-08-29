package org.rj.homectl.kafka.consumer.handlers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class DelegatingRecordHandler<K, V> implements ConsumerRecordsHandler<K, V> {
    private static final Logger LOG = LoggerFactory.getLogger(DelegatingRecordHandler.class);
    private final RecordInfoConsumer<K, V> delegate;

    public DelegatingRecordHandler(RecordInfoConsumer<K, V> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void process(ConsumerRecords<K, V> consumerRecords) {
        consumerRecords.forEach(x -> recordInfo(x).ifPresent(delegate::acceptRecord));
    }

    protected Optional<ConsumerRecordInfo<K, V>> recordInfo(ConsumerRecord<K, V> record) {
        try {
            return Optional.of(new ConsumerRecordInfo<>(record));
        }
        catch (Throwable t) {
            LOG.error(String.format("Failed to generate record info for consumer record (error: %s, record: %s)", t.getMessage(), record), t);
            return Optional.empty();
        }
    }
}
