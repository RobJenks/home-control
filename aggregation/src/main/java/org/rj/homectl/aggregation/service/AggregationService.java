package org.rj.homectl.aggregation.service;

import org.rj.homectl.aggregation.Aggregation;
import org.rj.homectl.kafka.consumer.handlers.ConsumerRecordInfo;
import org.rj.homectl.kafka.consumer.handlers.RecordInfoConsumer;
import org.rj.homectl.status.events.StatusEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AggregationService implements RecordInfoConsumer<String, StatusEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(AggregationService.class);
    private final Aggregation parent;

    public AggregationService(final Aggregation parent) {
        this.parent = parent;
    }

    @Override
    public void acceptRecord(final ConsumerRecordInfo<String, StatusEvent> recordInfo) {
        LOG.info("Got record");
    }
}
