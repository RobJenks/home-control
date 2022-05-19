package org.rj.homectl.aggregation.metrics;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.rj.homectl.aggregation.Aggregation;
import org.rj.homectl.common.config.Config;
import org.rj.homectl.common.config.ConfigEntry;
import org.rj.homectl.common.util.Util;
import org.rj.homectl.kafka.metrics.KafkaMetricsCollector;
import org.rj.homectl.kafka.metrics.beans.KafkaConsumerMetrics;
import org.rj.homectl.status.events.StatusEventType;
import org.rj.homectl.status.metrics.KafkaClusterMetricsData;
import org.rj.homectl.status.producer.StatusEventProducer;
import org.rj.homectl.status.producer.metrics.ClusterMetricsStatusEventProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class AggregationMetricsMonitor {
    private static final Logger log = LoggerFactory.getLogger(AggregationMetricsMonitor.class);

    private final Aggregation parent;
    private final Config config;
    private final ScheduledExecutorService monitorService;
    private final ClusterMetricsStatusEventProducer producer;
    private final Set<String> metricNames;
    private final AtomicReference<KafkaClusterMetricsData> metrics;

    public AggregationMetricsMonitor(final Aggregation parent, final Config config) {
        this.parent = parent;
        this.config = config;
        this.metricNames = metricsForCollection();
        this.metrics = new AtomicReference<>(new KafkaClusterMetricsData());

        // Create new kafka producer to publish cluster metrics
        this.producer = initialiseMetricsProducer();

        // Start a periodic job to collect metrics
        final var collectionIntervalSecs = config.getLong(ConfigEntry.MetricsMonitorConsumerCollectionIntervalSecs);
        monitorService = Executors.newSingleThreadScheduledExecutor();
        monitorService.scheduleAtFixedRate(this::collectMetrics, collectionIntervalSecs, collectionIntervalSecs, TimeUnit.SECONDS);
    }

    private Set<String> metricsForCollection() {
        return Set.of("records-consumed-total", "bytes-consumed-total", "records-lag-max", "fetch-rate", "fetch-total", "assigned-partitions",
                "fetch-latency-avg", "fetch-latency-max", "network-io-rate", "fetch-size-avg", "fetch-size-max",
                "records-per-request-avg", "records-per-request-max", "last-heartbeat-seconds-ago", "last-rebalance-seconds-ago", "rebalance-total");
    }

    private void collectMetrics() {
        if (parent.getStatusEventConsumer() == null) {
            log.error("Cannot collect aggregation metrics; required components are not valid");
            return;
        }

        // Consumer metrics
        final var metrics = KafkaMetricsCollector.collectNumericMetrics(parent.getStatusEventConsumer().getConsumerMetrics(), metricNames);
        final var consumerMetrics = Util.objectMapper().convertValue(metrics, KafkaConsumerMetrics.class);

        // Aggregate
        final var aggregationMetrics = new KafkaClusterMetricsData(consumerMetrics);

        // Store and replace current metrics
        this.metrics.set(aggregationMetrics);

        // Publish a new metrics event
        this.producer.send(StatusEventType.ClusterMetrics.getKey(), aggregationMetrics);
    }

    public KafkaClusterMetricsData getMetrics() {
        return metrics.get();
    }

    private ClusterMetricsStatusEventProducer initialiseMetricsProducer() {
        final var producerId = config.tryGet(ConfigEntry.MetricsMonitorProducerId)
                .orElseThrow(() -> new RuntimeException("Metrics monitor initialisation failed; no configured producer ID"));

        final var producerConfigPath = config.tryGet(ConfigEntry.MetricsMonitorProducerConfig)
                .orElseThrow(() -> new RuntimeException("Metrics monitor initialisation failed; no producer config provided"));

        return new ClusterMetricsStatusEventProducer(
                producerId,
                Config.load(producerConfigPath, config),
                KafkaProducer::new
        );
    }

    public void shutdown() {
        log.info("Shutting down background metrics monitor service");
        monitorService.shutdown(); // Prevent new tasks being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!monitorService.awaitTermination(60, TimeUnit.SECONDS)) {
                monitorService.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to be cancelled
                if (!monitorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    log.error("Metrics monitor service did not terminate correctly");
                }
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            monitorService.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

}
