package org.rj.homectl.kafka.consumer.handlers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConsumerRecordInfo<K, V> {
    private final String topic;
    private final int partition;
    private final long offset;
    private final long timestamp;
    private final K key;
    private final V value;
    private final Map<String, List<String>> headers;

    public ConsumerRecordInfo(ConsumerRecord<K, V> consumerRecord) {
        this.topic = consumerRecord.topic();
        this.partition = consumerRecord.partition();
        this.offset = consumerRecord.offset();
        this.timestamp = consumerRecord.timestamp();
        this.key = consumerRecord.key();
        this.value = consumerRecord.value();
        this.headers = Arrays.stream(consumerRecord.headers().toArray())
                .collect(Collectors.toMap(Header::key, h -> List.of(new String(h.value())),
                        (a, b) -> Stream.concat(a.stream(), b.stream()).collect(Collectors.toList())));
    }

    public String getTopic() {
        return topic;
    }

    public int getPartition() {
        return partition;
    }

    public long getOffset() {
        return offset;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }
}
