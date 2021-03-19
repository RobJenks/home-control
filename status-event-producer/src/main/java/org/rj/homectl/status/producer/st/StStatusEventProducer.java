package org.rj.homectl.status.producer.st;

import org.rj.homectl.common.config.Config;
import org.rj.homectl.common.util.Util;
import org.rj.homectl.kafka.producer.ProducerGenerator;
import org.rj.homectl.status.events.StatusEventType;
import org.rj.homectl.status.producer.StatusEventProducer;
import org.rj.homectl.status.serde.StatusEventMessage;
import org.rj.homectl.status.st.StStatusData;

import java.time.OffsetDateTime;
import java.util.Map;

public class StStatusEventProducer extends StatusEventProducer<StStatusData> {

    public StStatusEventProducer(String id, Config config, ProducerGenerator<String, StatusEventMessage> producerGenerator) {
        super(id, config, producerGenerator, StStatusEventProducer.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public StatusEventMessage prepareMessage(StStatusData data) {
        final var message = new StatusEventMessage();
        message.setTimestamp(OffsetDateTime.now());
        message.setType(StatusEventType.ST);
        message.setData(Util.objectMapper().convertValue(data, Map.class));

        return message;
    }
}
