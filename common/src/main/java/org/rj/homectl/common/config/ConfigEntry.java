package org.rj.homectl.common.config;

public enum ConfigEntry {
    ConsumerId("consumer.id"),
    ConsumerConfig("consumer.config"),
    ProducerId("producer.id"),
    ProducerConfig("producer.config"),
    InputTopicNames("input.topic.names"),
    PollDurationSec("poll.duration.secs"),
    AggregationConfig("aggregation.config"),
    AggregationLogInboundRecords("aggregation.logging.logInboundRecords"),
    AggregationMetricsPort("aggregation.metrics.port"),
    MonitorSensor("monitor.sensor"),
    MonitorSensors("monitor.sensors"),
    MonitorQuery("monitor.query"),
    MonitorPollIntervalMs("monitor.pollIntervalMs"),
    MonitorFullSnapshotSendIntervalMs("monitor.fullSnapshotSendIntervalMs"),
    MonitorToken("monitor.token"),
    MonitorRequestListing("monitor.requests.listing"),
    MonitorRequestStatus("monitor.requests.status"),
    MonitorStDevices("monitor.st.devices"),
    MetricsMonitorProducerId("aggregation.metricsMonitor.producer.id"),
    MetricsMonitorProducerConfig("aggregation.metricsMonitor.producer.config"),
    MetricsMonitorProducerPublishIntervalSecs("aggregation.metricsMonitor.producer.publishIntervalSecs"),
    MetricsMonitorConsumerCollectionIntervalSecs("aggregation.metricsMonitor.monitoredServices.consumer.collectionIntervalSecs");


    private final String key;
    ConfigEntry(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
