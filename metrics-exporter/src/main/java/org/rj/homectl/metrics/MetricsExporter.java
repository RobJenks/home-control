package org.rj.homectl.metrics;

import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.HTTPServer;
import org.apache.commons.lang3.StringUtils;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple;
import org.rj.homectl.metrics.util.MetricsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import static org.jooq.lambda.tuple.Tuple.*;
import static org.rj.homectl.metrics.util.MetricsConstants.*;

public class MetricsExporter {
    private static final Logger LOG = LoggerFactory.getLogger(MetricsExporter.class);

    private final Map<String, Gauge> numericMetrics = new ConcurrentHashMap<>();


    public MetricsExporter(int port) {
        initialiseMetricsServer(port);
        initialiseMetrics();
    }

    private void initialiseMetricsServer(int port) {
        try {
            LOG.info("Starting metrics export server...");
            new HTTPServer(port);

            LOG.info("Metrics export server started on port {}", port);
        }
        catch (IOException ex) {
            LOG.error(String.format("Failed to start metrics export server on port %d (%s)", port, ex.getMessage()), ex);
        }
    }

    private void initialiseMetrics() {
        initialiseEnvironmentMetrics();

    }

    private void initialiseEnvironmentMetrics() {
        final var metrics = Seq.of(
                tuple(METRIC_ENV_SCORE, "Environment score"),
                tuple(METRIC_ENV_TEMP, "Air temperature"),
                tuple(METRIC_ENV_HUMIDITY, "Humidity"),
                tuple(METRIC_ENV_ABS_HUMIDITY, "Absolute humidity"),
                tuple(METRIC_ENV_DEW_POINT, "Dew point"),
                tuple(METRIC_ENV_CO2, "CO2"),
                tuple(METRIC_ENV_EST_CO2, "Estimated CO2"),
                tuple(METRIC_ENV_VOC, "Volatile Organic Compounds (VOCs)"),
                tuple(METRIC_ENV_VOC_BASELINE, "VOC baseline"),
                tuple(METRIC_ENV_VOC_RAW_H2, "VOC raw H2"),
                tuple(METRIC_ENV_VOC_RAW_ETHANOL, "VOC raw Ethanol"),
                tuple(METRIC_ENV_PM25, "PM2.5 particulates"),
                tuple(METRIC_ENV_EST_PM10, "Estimated PM1.0 particulates")
        );

        metrics.map(metric -> tuple(metric.v1, newGauge(metric.v1, metric.v2, List.of("room", "device"))))
                .forEach(namedMetric -> registerNumericMetric(namedMetric.v1, namedMetric.v2));
    }

    private void registerNumericMetric(String name, Gauge metric) {
        if (StringUtils.isBlank(name) || metric == null) throw new RuntimeException("Invalid metric, cannot register");

        if (numericMetrics.containsKey(name)) throw new IllegalArgumentException(
                String.format("Cannot create metric; metric already exists with name '%s'", name));

        numericMetrics.put(name, metric);
    }

    private Gauge newGauge(String name, String desc, List<String> labels) {
        final var labelNames = labels.toArray(new String[0]);
        return Gauge.build().name(MetricsConstants.NAMESPACE).name(name).help(desc).labelNames(labelNames).register();
    }

    private Optional<Gauge> getValidatedNumericMetric(String name) {
        if (StringUtils.isBlank(name)) {
            LOG.warn("Cannot record metric with missing name");
            return Optional.empty();
        }

        final var metric = numericMetrics.get(name);
        if (metric == null) {
            LOG.warn("Cannot record metric value; no metric exists with name '{}'", name);
            return Optional.empty();
        }

        return Optional.of(metric);
    }

    public void setNumericMetric(String name, List<String> labels, double value) {
        getValidatedNumericMetric(name)
                .ifPresent(metric -> metric
                        .labels(labels.toArray(new String[0]))
                        .set(value));
    }

}
