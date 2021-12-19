package org.rj.homectl.aggregation.processor;

import org.rj.homectl.aggregation.state.AggregateState;
import org.rj.homectl.awair.model.device.AwairDeviceState;
import org.rj.homectl.common.model.Device;
import org.rj.homectl.common.model.DeviceClass;
import org.rj.homectl.metrics.MetricsExporter;
import org.rj.homectl.metrics.util.MetricsConstants;
import org.rj.homectl.status.awair.AwairStatusData;
import org.rj.homectl.status.awair.AwairStatusEvent;

import java.util.List;

public class AwairEventProcessor extends AbstractEventProcessor<AwairStatusEvent> {

    public AwairEventProcessor(final AggregateState state, final MetricsExporter metricsExporter) {
        super(state, metricsExporter);
    }

    @Override
    public void processEvent(AwairStatusEvent event) {
        final var device = getState().getDevice(event.getData().getDeviceId());
        final var deviceState = getState().getDeviceState(event.getData().getDeviceId(),
                DeviceClass.Awair, AwairDeviceState::new);

        // Update current state
        deviceState.ifPresent(currentState -> currentState.updateFromStatus(event.getData()));

        // Record metrics for the specific event
        device.ifPresent(dev -> recordMetrics(event, dev));

    }

    private void recordMetrics(AwairStatusEvent event, Device device) {
        final var labels = List.of(device.getRoom(), device.getName());

        getMetricsExporter().setNumericMetric(MetricsConstants.METRIC_ENV_SCORE, labels, event.getData().getScore());
        getMetricsExporter().setNumericMetric(MetricsConstants.METRIC_ENV_TEMP, labels, event.getData().getTemp());
        getMetricsExporter().setNumericMetric(MetricsConstants.METRIC_ENV_HUMIDITY, labels, event.getData().getHumidity());
        getMetricsExporter().setNumericMetric(MetricsConstants.METRIC_ENV_ABS_HUMIDITY, labels, event.getData().getAbsoluteHumidity());
        getMetricsExporter().setNumericMetric(MetricsConstants.METRIC_ENV_DEW_POINT, labels, event.getData().getDewPoint());
        getMetricsExporter().setNumericMetric(MetricsConstants.METRIC_ENV_CO2, labels, event.getData().getCo2());
        getMetricsExporter().setNumericMetric(MetricsConstants.METRIC_ENV_EST_CO2, labels, event.getData().getEstimatedCo2());
        getMetricsExporter().setNumericMetric(MetricsConstants.METRIC_ENV_VOC, labels, event.getData().getVoc());
        getMetricsExporter().setNumericMetric(MetricsConstants.METRIC_ENV_VOC_BASELINE, labels, event.getData().getVocBaseline());
        getMetricsExporter().setNumericMetric(MetricsConstants.METRIC_ENV_VOC_RAW_H2, labels, event.getData().getVocRawH2());
        getMetricsExporter().setNumericMetric(MetricsConstants.METRIC_ENV_VOC_RAW_ETHANOL, labels, event.getData().getVocRawEthanol());
        getMetricsExporter().setNumericMetric(MetricsConstants.METRIC_ENV_PM25, labels, event.getData().getPm25());
        getMetricsExporter().setNumericMetric(MetricsConstants.METRIC_ENV_EST_PM10, labels, event.getData().getEstimatedPm10());
    }
}
