package org.rj.homectl.consumer.status;

import org.apache.kafka.clients.consumer.Consumer;
import org.rj.homectl.common.beans.events.StatusEvent;
import org.rj.homectl.common.config.Config;
import org.rj.homectl.kafka.consumer.AbstractEventConsumer;
import org.rj.homectl.kafka.consumer.handlers.ConsumerRecordsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatusEventConsumer extends AbstractEventConsumer<String, StatusEvent> {

    @Autowired
    public StatusEventConsumer(final String id,
                               final Config config,
                               final Consumer<String, StatusEvent> consumer,
                               final ConsumerRecordsHandler<String, StatusEvent> recordsHandler) {
        super(id, config, consumer, recordsHandler, StatusEventConsumer.class);
    }
}

*** Add something similar to this for custom consumer factory: "https://github.com/SpringOnePlatform2016/grussell-spring-kafka/blob/master/s1p-kafka/src/main/java/org/s1p/JsonConfiguration.java#L66"