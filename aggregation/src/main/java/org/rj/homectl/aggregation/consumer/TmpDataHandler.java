package org.rj.homectl.aggregation.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.rj.homectl.aggregation.controller.TmpData;
import org.rj.homectl.kafka.consumer.ConsumerRecordsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TmpDataHandler implements ConsumerRecordsHandler<String, String> {
    private static final Logger LOG = LoggerFactory.getLogger(TmpDataHandler.class);

    @Override
    public void process(ConsumerRecords<String, String> consumerRecords) {
        if (consumerRecords.isEmpty()) return;

        LOG.info("Processing consumer record batch ({}) items", consumerRecords.count());
        consumerRecords.forEach(x -> {
            final var value = String.format("%s: %s [%d / %d]", x.key(), x.value(), x.offset(), x.timestamp());

            LOG.info("Processing new record ({})", value);
            TmpData.recordData(value);
        });
    }
}
