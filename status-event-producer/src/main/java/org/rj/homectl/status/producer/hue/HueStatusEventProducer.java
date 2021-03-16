package org.rj.homectl.status.producer.hue;

import org.rj.homectl.common.config.Config;
import org.rj.homectl.common.util.Util;
import org.rj.homectl.kafka.producer.ProducerGenerator;
import org.rj.homectl.status.awair.AwairStatusData;
import org.rj.homectl.status.events.StatusEventType;
import org.rj.homectl.status.hue.HueStatusData;
import org.rj.homectl.status.producer.StatusEventProducer;
import org.rj.homectl.status.serde.StatusEventMessage;

import java.time.OffsetDateTime;
import java.util.Map;

public class HueStatusEventProducer extends StatusEventProducer<HueStatusData> {

    public HueStatusEventProducer(String id, Config config, ProducerGenerator<String, StatusEventMessage> producerGenerator) {
        super(id, config, producerGenerator, HueStatusEventProducer.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public StatusEventMessage prepareMessage(HueStatusData data) {
        final var message = new StatusEventMessage();
        message.setTimestamp(OffsetDateTime.now());
        message.setType(StatusEventType.Hue);
        message.setData(Util.objectMapper().convertValue(data, Map.class));

        return message;
    }
}
