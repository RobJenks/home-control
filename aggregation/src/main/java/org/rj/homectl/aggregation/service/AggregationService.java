package org.rj.homectl.aggregation.service;

import org.rj.homectl.aggregation.Aggregation;
import org.rj.homectl.aggregation.state.AggregateState;
import org.rj.homectl.awair.model.device.AwairDeviceState;
import org.rj.homectl.common.config.Config;
import org.rj.homectl.common.config.ConfigEntry;
import org.rj.homectl.common.model.DeviceClass;
import org.rj.homectl.common.model.Home;
import org.rj.homectl.common.model.HomeState;
import org.rj.homectl.common.util.Util;
import org.rj.homectl.kafka.consumer.handlers.ConsumerRecordInfo;
import org.rj.homectl.kafka.consumer.handlers.RecordInfoConsumer;
import org.rj.homectl.status.awair.AwairStatusEvent;
import org.rj.homectl.status.events.StatusEvent;
import org.rj.homectl.status.hue.HueStatusEvent;
import org.rj.homectl.status.st.StStatusEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AggregationService implements RecordInfoConsumer<String, StatusEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(AggregationService.class);
    private final Aggregation parent;
    private final Config config;
    private final AggregateState state;

    public AggregationService(final Aggregation parent, final Config config) {
        this.parent = parent;
        this.config = config;
        this.state = initialiseState(config.get(ConfigEntry.AggregationConfig));
    }

    private AggregateState initialiseState(String configLocation) {
        return Util.loadStringResource(configLocation)
                .flatMap(config -> Util.deserializeYaml(config, Home.class))
                .map(Home::getHome)
                .map(AggregateState::new)
                .orElseThrow(e -> new RuntimeException(String.format("Failed to load aggregation state config (%s)", e)));
    }

    public HomeState getState() {
        return state.getData();
    }

    @Override
    public void acceptRecord(final ConsumerRecordInfo<String, StatusEvent> recordInfo) {
        final var event = recordInfo.getValue();

        if (event instanceof AwairStatusEvent)      processEvent((AwairStatusEvent) event);
        else if (event instanceof HueStatusEvent)   processEvent((HueStatusEvent) event);
        else if (event instanceof StStatusEvent)    processEvent((StStatusEvent) event);
        else {
            LOG.error("Received unrecognised status event type for aggregation: {}", Util.safeSerialize(event));
        }
    }

    public void processEvent(AwairStatusEvent event) {
        final var locatedDevice = state.getDevice(event.getData().getDeviceId(), DeviceClass.Awair);
        if (locatedDevice.isEmpty()) {
            // This is a new device not currently present in aggregated state
            return;
        }

        final var device = locatedDevice.get();
        if (device.getState() == null) device.setState(new AwairDeviceState());

        ((AwairDeviceState)device.getState()).updateFromStatus(event.getData());
    }

    public void processEvent(HueStatusEvent event) {
        // TODO
    }

    public void processEvent(StStatusEvent event) {
        // TODO
    }



}
