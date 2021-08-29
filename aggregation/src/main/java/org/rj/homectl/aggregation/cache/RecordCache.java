package org.rj.homectl.aggregation.cache;

import org.rj.homectl.aggregation.Aggregation;
import org.rj.homectl.common.cache.RingBufferCache;
import org.rj.homectl.kafka.consumer.handlers.ConsumerRecordInfo;
import org.rj.homectl.kafka.consumer.handlers.RecordInfoConsumer;
import org.rj.homectl.status.events.StatusEvent;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class RecordCache implements RecordInfoConsumer<String, StatusEvent> {
    private static final int CACHE_SIZE = 100;
    private final AtomicReference<RingBufferCache<ConsumerRecordInfo<String, StatusEvent>>> cache;
    private final Aggregation parent;

    public RecordCache(final Aggregation parent) {
        this.cache = new AtomicReference<>(new RingBufferCache<>(CACHE_SIZE, () -> null));
        this.parent = parent;
    }

    @Override
    public void acceptRecord(final ConsumerRecordInfo<String, StatusEvent> recordInfo) {
        cache.get().add(recordInfo);
    }

    public AtomicReference<RingBufferCache<ConsumerRecordInfo<String, StatusEvent>>> getData() {
        return cache;
    }

    public List<ConsumerRecordInfo<String, StatusEvent>> getLatestRecords(int count) {
        if (count < 0) throw new IllegalArgumentException("Invalid record count requested");
        return getData()
                .get()
                .reverseStream()
                .limit(count)
                .collect(Collectors.toList());
    }
}
