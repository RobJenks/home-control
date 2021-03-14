package org.rj.homectl.status.producer.awair;

import org.rj.homectl.common.config.Config;
import org.rj.homectl.common.util.Util;
import org.rj.homectl.kafka.producer.ProducerGenerator;
import org.rj.homectl.status.awair.AwairStatusData;
import org.rj.homectl.status.events.StatusEventType;
import org.rj.homectl.status.producer.StatusEventProducer;
import org.rj.homectl.status.serde.StatusEventMessage;

import java.util.Map;

public class AwairStatusEventProducer extends StatusEventProducer<AwairStatusData> {

    public AwairStatusEventProducer(String id, Config config, ProducerGenerator<String, StatusEventMessage> producerGenerator, Class<?> implementationClass) {
        super(id, config, producerGenerator, implementationClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public StatusEventMessage prepareMessage(AwairStatusData data) {
        final var message = new StatusEventMessage();
        message.setTimestamp(data.getTimestamp());
        message.setType(StatusEventType.Awair);
        message.setData(Util.objectMapper().convertValue(data, Map.class));

        return message;
    }
}
