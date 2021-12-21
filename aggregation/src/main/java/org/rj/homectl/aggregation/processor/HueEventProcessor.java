package org.rj.homectl.aggregation.processor;

import org.rj.homectl.aggregation.state.AggregateState;
import org.rj.homectl.common.model.DeviceClass;
import org.rj.homectl.common.util.Util;
import org.rj.homectl.hue.model.state.HueDeviceState;
import org.rj.homectl.metrics.MetricsExporter;
import org.rj.homectl.metrics.util.MetricsConstants;
import org.rj.homectl.status.hue.HueEventType;
import org.rj.homectl.status.hue.HueStatusEvent;
import org.rj.homectl.status.hue.HueStatusLightData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static org.jooq.lambda.tuple.Tuple.*;

public class HueEventProcessor extends AbstractEventProcessor<HueStatusEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(HueEventProcessor.class);

    public HueEventProcessor(final AggregateState state, final MetricsExporter metricsExporter) {
        super(state, metricsExporter);
    }

    @Override
    public void processEvent(HueStatusEvent event) {
        final var eventType = event.getData().getType();
        if (eventType == HueEventType.Snapshot || eventType == HueEventType.Update) {
            // Event includes full status of one or more light devices, so reflect in the aggregated state
            processLightStateData(event.getData().getLights());
        }
        else if (eventType == HueEventType.Event) {
            // Records a specific device event, e.g. a light being switched off
        }
        else {
            LOG.error("Received unrecognised Hue event type for aggregation: {}", Util.safeSerialize(event));
        }

        // Record metrics when each event is received
        recordMetrics(event);
    }

    private void processLightStateData(HueStatusLightData data) {
        if (data == null) return;

        data.entrySet().stream()
                .map(x -> tuple(x, getState().getDeviceState(x.getValue().getName(), DeviceClass.Hue, HueDeviceState::new)))
                .filter(x -> x.v2.isPresent())
                .forEach(x -> x.v2.get().updateFromStatus(x.v1.getKey(), x.v1.getValue()));
    }

    private void recordMetrics(HueStatusEvent event) {
        final var deviceStates = getState().getDevices().stream()
                .filter(x -> x.getDeviceClass() == DeviceClass.Hue)
                .map(x -> tuple(x, (HueDeviceState)x.getState()))
                .collect(Collectors.toList());

        // Device enabled metric
        deviceStates.forEach(x -> getMetricsExporter().setNumericMetric(
                MetricsConstants.METRIC_HUE_DEVICES_ON,
                List.of(x.v1.getRoom(), x.v1.getName()),
                x.v2.isOn() ? 1.0 : 0.0));
    }
}
