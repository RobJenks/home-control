package org.rj.homectl.aggregation.processor;

import org.rj.homectl.aggregation.state.AggregateState;
import org.rj.homectl.metrics.MetricsExporter;
import org.rj.homectl.metrics.util.MetricsConstants;
import org.rj.homectl.status.metrics.KafkaClusterMetricsData;
import org.rj.homectl.status.metrics.KafkaClusterMetricsStatusEvent;

import java.util.List;

public class ClusterMetricsEventProcessor extends AbstractEventProcessor<KafkaClusterMetricsStatusEvent> {

    public ClusterMetricsEventProcessor(AggregateState state, MetricsExporter metricsExporter) {
        super(state, metricsExporter);
    }

    @Override
    public void processEvent(KafkaClusterMetricsStatusEvent event) {
        publishClusterMetrics(event.getData());
    }

    private void publishClusterMetrics(KafkaClusterMetricsData data) {
        // Consumer metrics
        final var consumer = data.getConsumerMetrics();
        getMetricsExporter().setNumericMetric(MetricsConstants.METRIC_CLUSTER_CONSUMER_BYTES_CONSUMED, List.of(), consumer.getBytesConsumedTotal());
        getMetricsExporter().setNumericMetric(MetricsConstants.METRIC_CLUSTER_CONSUMER_FETCH_LATENCY, List.of("avg"), consumer.getFetchLatencyAvg());
        getMetricsExporter().setNumericMetric(MetricsConstants.METRIC_CLUSTER_CONSUMER_FETCH_LATENCY, List.of("max"), consumer.getFetchLatencyMax());
        getMetricsExporter().setNumericMetric(MetricsConstants.METRIC_CLUSTER_CONSUMER_FETCH_RATE, List.of(), consumer.getFetchRate());
        getMetricsExporter().setNumericMetric(MetricsConstants.METRIC_CLUSTER_CONSUMER_FETCH_SIZE, List.of("avg"), consumer.getFetchSizeAvg());
        getMetricsExporter().setNumericMetric(MetricsConstants.METRIC_CLUSTER_CONSUMER_FETCH_SIZE, List.of("max"), consumer.getFetchSizeMax());
        getMetricsExporter().setNumericMetric(MetricsConstants.METRIC_CLUSTER_CONSUMER_FETCH_TOTAL, List.of(), consumer.getFetchTotal());
        getMetricsExporter().setNumericMetric(MetricsConstants.METRIC_CLUSTER_CONSUMER_LAST_HEARTBEAT_SECS, List.of(), consumer.getLastHeartbeatSecondsAgo());
        getMetricsExporter().setNumericMetric(MetricsConstants.METRIC_CLUSTER_CONSUMER_NETWORK_IO_RATE, List.of(), consumer.getNetworkIoRate());
        getMetricsExporter().setNumericMetric(MetricsConstants.METRIC_CLUSTER_CONSUMER_RECORD_LAG_MAX, List.of(), consumer.getRecordsLagMax());
        getMetricsExporter().setNumericMetric(MetricsConstants.METRIC_CLUSTER_CONSUMER_RECORDS_CONSUMED, List.of(), consumer.getRecordsConsumedTotal());
    }
}
