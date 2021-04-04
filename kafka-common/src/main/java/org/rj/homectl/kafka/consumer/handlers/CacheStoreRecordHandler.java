package org.rj.homectl.kafka.consumer.handlers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.rj.homectl.common.cache.RingBufferCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class CacheStoreRecordHandler<K, V> implements ConsumerRecordsHandler<K, V> {
    private static final Logger LOG = LoggerFactory.getLogger(CacheStoreRecordHandler.class);

    private final AtomicReference<RingBufferCache<ConsumerRecordInfo<K, V>>> cache;

    public CacheStoreRecordHandler(AtomicReference<RingBufferCache<ConsumerRecordInfo<K, V>>> cache) {
        this.cache = cache;
    }

    @Override
    public void process(ConsumerRecords<K, V> consumerRecords) {
        consumerRecords.forEach(x -> recordInfo(x).ifPresent(
                recordInfo -> cache.get().add(recordInfo)));
    }

    private Optional<ConsumerRecordInfo<K, V>> recordInfo(ConsumerRecord<K, V> record) {
        try {
            return Optional.of(new ConsumerRecordInfo<>(record));
        }
        catch (Throwable t) {
            LOG.error(String.format("Failed to generate record info for consumer record (error: %s, record: %s)", t.getMessage(), record), t);
            return Optional.empty();
        }
    }
}
