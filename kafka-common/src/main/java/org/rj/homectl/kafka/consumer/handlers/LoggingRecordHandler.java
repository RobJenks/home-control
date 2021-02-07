package org.rj.homectl.aggregation.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.rj.homectl.aggregation.controller.TmpData;
import org.rj.homectl.kafka.consumer.handlers.ConsumerRecordsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingRecordHandler<K, V> implements ConsumerRecordsHandler<K, V> {
    private static final Logger LOG = LoggerFactory.getLogger(LoggingRecordHandler.class);

    @Override
    public void process(ConsumerRecords<K, V> consumerRecords) {
        if (consumerRecords.isEmpty()) return;

        consumerRecords.forEach(x -> {
            final var value = String.format("%s: %s [%d / %d]", x.key(), x.value(), x.offset(), x.timestamp());

            LOG.info("Processing new record ({})", value);
            TmpData.recordData(value);
        });
    }
}
