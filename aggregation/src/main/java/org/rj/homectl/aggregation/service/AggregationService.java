package org.rj.homectl.aggregation.service;

import org.rj.homectl.aggregation.Aggregation;
import org.rj.homectl.common.config.Config;
import org.rj.homectl.common.config.ConfigEntry;
import org.rj.homectl.common.model.Home;
import org.rj.homectl.common.model.HomeState;
import org.rj.homectl.common.util.Util;
import org.rj.homectl.kafka.consumer.handlers.ConsumerRecordInfo;
import org.rj.homectl.kafka.consumer.handlers.RecordInfoConsumer;
import org.rj.homectl.status.events.StatusEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AggregationService implements RecordInfoConsumer<String, StatusEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(AggregationService.class);
    private final Aggregation parent;
    private final Config config;
    private final HomeState state;

    public AggregationService(final Aggregation parent, final Config config) {
        this.parent = parent;
        this.config = config;
        this.state = initialiseState(config.get(ConfigEntry.AggregationConfig));

        LOG.info("AGG CONFIG: {}", Util.safeSerialize(state));
    }

    private HomeState initialiseState(String configLocation) {
        return Util.loadStringResource(configLocation)
                .flatMap(config -> Util.deserializeYaml(config, Home.class))
                .map(Home::getHome)
                .orElseThrow(e -> new RuntimeException(String.format("Failed to load aggregation state config (%s)", e)));
    }

    @Override
    public void acceptRecord(final ConsumerRecordInfo<String, StatusEvent> recordInfo) {
        LOG.info("Got record");
    }
}
