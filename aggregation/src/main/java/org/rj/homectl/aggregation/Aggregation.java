package org.rj.homectl.aggregation;

import org.rj.homectl.aggregation.cache.RecordCache;
import org.rj.homectl.aggregation.controller.AggregationController;
import org.rj.homectl.aggregation.service.AggregationService;
import org.rj.homectl.common.cache.RingBufferCache;
import org.rj.homectl.common.config.Config;
import org.rj.homectl.common.config.ConfigEntry;
import org.rj.homectl.kafka.consumer.handlers.*;
import org.rj.homectl.service.ServiceBase;
import org.rj.homectl.spring.application.SpringApplicationContext;
import org.rj.homectl.status.consumer.StatusEventConsumer;
import org.rj.homectl.status.events.StatusEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;


@SpringBootApplication
@ComponentScan(basePackages = "org.rj")
public class Aggregation extends ServiceBase {
    private AggregationController aggregationController;
    private AggregationService aggregationService;
    private RecordCache recordCache;

    public static void main(String[] args) {
        SpringApplication.run(Aggregation.class, args);
    }

    public Aggregation(SpringApplicationContext context) {
        super(Aggregation.class, context);
    }

    @PostConstruct
    private void initialise() {
        this.aggregationController = new AggregationController(this);
        this.aggregationService = new AggregationService(this);
        this.recordCache = new RecordCache(this);

        final var consumerId = getConfig().get(ConfigEntry.ConsumerId);
        final var consumerConfigPath = getConfig().get(ConfigEntry.ConsumerConfig);

        final var statusConsumer = new StatusEventConsumer(
                consumerId,
                Config.load(consumerConfigPath, getConfig()),
                Optional.empty(),
                Optional.of(buildRecordHandler()));

        new Thread(statusConsumer::execute).start();
    }

    private ConsumerRecordsHandler<String, StatusEvent> buildRecordHandler() {
        final CompositeRecordsHandler<String, StatusEvent> handler = new CompositeRecordsHandler<>();

        handler.addHandler(new DelegatingRecordHandler<>(aggregationService));
        handler.addHandler(new DelegatingRecordHandler<>(recordCache));

        if (getConfig().tryGetBoolean(ConfigEntry.LogInboundRecords).orElse(false)) {
            handler.addHandler(new LoggingRecordHandler<>());
        }

        return handler;
    }

    @Override
    protected boolean handleTerminationRequest(String reason) {
        return true;    // accept all requests
    }

    public AggregationService getAggregationService() {
        return aggregationService;
    }

    public RecordCache getRecordCache() {
        return recordCache;
    }
}