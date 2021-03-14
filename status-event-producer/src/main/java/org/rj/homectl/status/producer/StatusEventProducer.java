package org.rj.homectl.status.producer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.rj.homectl.common.config.Config;
import org.rj.homectl.kafka.producer.AbstractEventProducer;
import org.rj.homectl.kafka.producer.ProducerGenerator;
import org.rj.homectl.status.serde.StatusEventMessage;

public abstract class StatusEventProducer<T> extends AbstractEventProducer<String, StatusEventMessage> {

    public StatusEventProducer(String id, Config config, ProducerGenerator<String, StatusEventMessage> producerGenerator, Class<?> implementationClass) {
        super(id, config, producerGenerator, implementationClass);
    }

    @Override
    protected Class<?> getKeyClass() {
        return String.class;
    }

    @Override
    protected Class<?> getValueClass() {
        return StatusEventMessage.class;
    }

    public abstract StatusEventMessage prepareMessage(T data);

    public void send(String key, T data) {
        final var message = prepareMessage(data);

        this.getProducer().send(new ProducerRecord<>(getConfig().get("output.topic.name"), key, message));
    }
}
