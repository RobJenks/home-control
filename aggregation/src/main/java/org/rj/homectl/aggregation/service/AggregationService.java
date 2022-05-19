package org.rj.homectl.aggregation.service;

import org.rj.homectl.aggregation.Aggregation;
import org.rj.homectl.aggregation.processor.AwairEventProcessor;
import org.rj.homectl.aggregation.processor.ClusterMetricsEventProcessor;
import org.rj.homectl.aggregation.processor.HueEventProcessor;
import org.rj.homectl.aggregation.processor.StEventProcessor;
import org.rj.homectl.aggregation.state.AggregateState;
import org.rj.homectl.common.config.Config;
import org.rj.homectl.common.config.ConfigEntry;
import org.rj.homectl.common.model.Home;
import org.rj.homectl.common.model.HomeState;
import org.rj.homectl.common.util.Util;
import org.rj.homectl.kafka.consumer.handlers.ConsumerRecordInfo;
import org.rj.homectl.kafka.consumer.handlers.RecordInfoConsumer;
import org.rj.homectl.metrics.MetricsExporter;
import org.rj.homectl.status.awair.AwairStatusEvent;
import org.rj.homectl.status.events.StatusEvent;
import org.rj.homectl.status.hue.HueStatusEvent;
import org.rj.homectl.status.metrics.KafkaClusterMetricsStatusEvent;
import org.rj.homectl.status.st.StStatusEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AggregationService implements RecordInfoConsumer<String, StatusEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(AggregationService.class);
    private final Aggregation parent;
    private final Config config;
    private final AggregateState state;
    private final AwairEventProcessor awairEventProcessor;
    private final HueEventProcessor hueEventProcessor;
    private final StEventProcessor stEventProcessor;
    private final ClusterMetricsEventProcessor clusterMetricsEventProcessor;
    private final MetricsExporter metricsExporter;

    public AggregationService(final Aggregation parent, final Config config) {
        this.parent = parent;
        this.config = config;
        this.state = initialiseState(config.get(ConfigEntry.AggregationConfig));

        this.metricsExporter = new MetricsExporter(config.getInteger(ConfigEntry.AggregationMetricsPort));

        this.awairEventProcessor = new AwairEventProcessor(this.state, this.metricsExporter);
        this.hueEventProcessor = new HueEventProcessor(this.state, this.metricsExporter);
        this.stEventProcessor = new StEventProcessor(this.state, this.metricsExporter);
        this.clusterMetricsEventProcessor = new ClusterMetricsEventProcessor(this.state, this.metricsExporter);
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

        if (event instanceof AwairStatusEvent)                      awairEventProcessor.processEvent((AwairStatusEvent) event);
        else if (event instanceof HueStatusEvent)                   hueEventProcessor.processEvent((HueStatusEvent) event);
        else if (event instanceof StStatusEvent)                    stEventProcessor.processEvent((StStatusEvent) event);
        else if (event instanceof KafkaClusterMetricsStatusEvent)   clusterMetricsEventProcessor.processEvent((KafkaClusterMetricsStatusEvent) event);
        else {
            LOG.error("Received unrecognised status event type for aggregation: {}", Util.safeSerialize(event));
        }
    }


}
