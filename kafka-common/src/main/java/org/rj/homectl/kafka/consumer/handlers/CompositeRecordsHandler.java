package org.rj.homectl.kafka.consumer.handlers;

import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.util.ArrayList;
import java.util.List;

public class CompositeRecordsHandler<K, V> implements ConsumerRecordsHandler<K, V> {
    private List<ConsumerRecordsHandler<K, V>> handlers;

    public CompositeRecordsHandler() {
        this(List.of());
    }

    public CompositeRecordsHandler(List<ConsumerRecordsHandler<K, V>> handlers) {
        this.handlers = new ArrayList<>(handlers);
    }

    public void addHandler(ConsumerRecordsHandler<K, V> handler) {
        if (handler == null) throw new IllegalArgumentException("Cannot register null record handler");
        this.handlers.add(handler);
    }

    @Override
    public void process(ConsumerRecords<K, V> consumerRecords) {
        handlers.forEach(x -> x.process(consumerRecords));
    }
}
