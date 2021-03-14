package org.rj.homectl.kafka.consumer.handlers;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.rj.homectl.common.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingRecordHandler<K, V> implements ConsumerRecordsHandler<K, V> {
    private static final Logger LOG = LoggerFactory.getLogger(LoggingRecordHandler.class);

    @Override
    public void process(ConsumerRecords<K, V> consumerRecords) {
        if (consumerRecords.isEmpty()) return;

        consumerRecords.forEach(x -> {
            String value;
            try {
                final var recordInfo = new ConsumerRecordInfo<>(x);
                value = Util.objectMapper().writeValueAsString(recordInfo);
            }
            catch (Exception ex) {
                value = String.format("Failed to serialize consumer record (%s)", ex.toString());
            }

            LOG.info("Processing new record ({})", value);
            TmpData.recordData(value);
        });
    }
}
